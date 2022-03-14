/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.rabbitmq.util;

import io.github.smart.cloud.starter.rabbitmq.MqConstants;
import io.github.smart.cloud.starter.rabbitmq.adapter.IRabbitMqAdapter;
import io.github.smart.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import io.github.smart.cloud.starter.rabbitmq.enums.RetryResult;
import io.github.smart.cloud.utility.JacksonUtil;
import io.github.smart.cloud.utility.RetryTimeUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.AnnotationUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * mq工具类
 *
 * @author collin
 * @date 2021-06-30
 */
@Slf4j
public final class MqUtil {

    /**
     * 消费失败后是否能重试
     */
    @Setter
    private static boolean enableRetryAfterConsumerFail = false;

    private MqUtil() {
    }

    /**
     * 发送消息
     *
     * <p>delayTime取值example：String.valueOf(TimeUnit.SECONDS.toMillis(delayMillis))</p>
     *
     * @param rabbitMqAdapter
     * @param exchange
     * @param routingKey
     * @param object
     * @param retriedTimes
     * @param delayMillis
     * @param <T>
     */
    public static <T> void send(IRabbitMqAdapter rabbitMqAdapter, String exchange, String routingKey, T object, Integer retriedTimes, Long delayMillis) {
        String json = JacksonUtil.toJson(object);
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        MessageBuilder messageBuilder = MessageBuilder.withBody(body);
        if (delayMillis != null && delayMillis > 0) {
            messageBuilder.setHeader(MessageProperties.X_DELAY, delayMillis);
        }
        if (retriedTimes != null) {
            messageBuilder.setHeader(MqConstants.CONSUMER_RETRIED_TIMES, retriedTimes);
        }

        if (log.isInfoEnabled()) {
            log.info("mq.send|exchange={}, routingKey={}, delayMillis={}, retriedTimes={}, msg={}", exchange, routingKey, delayMillis, retriedTimes, json);
        }
        rabbitMqAdapter.send(exchange, routingKey, messageBuilder.build());
    }

    /**
     * 消费失败重试
     *
     * @param rabbitMqAdapter
     * @param object
     * @param message
     * @param consumerClass
     * @param <T>
     * @return
     */
    public static <T> RetryResult retryAfterConsumerFail(IRabbitMqAdapter rabbitMqAdapter, T object, Message message, Class<?> consumerClass) {
        if (!MqUtil.enableRetryAfterConsumerFail) {
            return RetryResult.NOT_SUPPORT;
        }
        if (object == null) {
            return RetryResult.NOT_SUPPORT;
        }

        RabbitListener rabbitListener = AnnotationUtils.findAnnotation(consumerClass, RabbitListener.class);
        if (rabbitListener == null) {
            return RetryResult.NOT_SUPPORT;
        }

        // 失败后，发送延迟消息重试
        MqConsumerFailRetry mqConsumerFailRetry = AnnotationUtils.findAnnotation(consumerClass, MqConsumerFailRetry.class);
        if (mqConsumerFailRetry == null) {
            log.warn("MqConsumerFailRetry not found, retry is skipped!");
            return RetryResult.NOT_SUPPORT;
        }
        Integer retriedTimes = message.getMessageProperties().getHeader(MqConstants.CONSUMER_RETRIED_TIMES);
        retriedTimes = (retriedTimes == null) ? 0 : (retriedTimes + 1);
        if (retriedTimes >= mqConsumerFailRetry.maxRetryTimes()) {
            log.warn("Maximum times of retries reached");
            return RetryResult.REACHED_RETRY_THRESHOLD;
        }

        // 队列的名称
        String queueName = rabbitListener.queues()[0];
        //延迟交换机名称
        String retryExchangeName = MqNameUtil.getRetryExchangeName(queueName, mqConsumerFailRetry);
        //延迟路由键
        String retryRouteKeyName = MqNameUtil.getRetryRouteKeyName(queueName, mqConsumerFailRetry);
        send(rabbitMqAdapter, retryExchangeName, retryRouteKeyName, object, retriedTimes, TimeUnit.SECONDS.toMillis(RetryTimeUtil.getNextExecuteTime(retriedTimes)));
        return RetryResult.SUCCESS;
    }

}