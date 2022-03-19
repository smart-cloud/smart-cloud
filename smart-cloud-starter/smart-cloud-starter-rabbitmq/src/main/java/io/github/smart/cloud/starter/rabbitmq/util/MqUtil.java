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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
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
        Message message = null;
        if (object instanceof Message) {
            message = (Message) object;
        } else {
            message = rabbitMqAdapter.getMessageConverter().toMessage(object, new MessageProperties());
        }

        MessageProperties messageProperties = message.getMessageProperties();
        if (delayMillis != null && delayMillis > 0) {
            messageProperties.setHeader(MessageProperties.X_DELAY, delayMillis);
        }
        if (retriedTimes != null) {
            messageProperties.setHeader(MqConstants.CONSUMER_RETRIED_TIMES, retriedTimes);
        }
        if (log.isInfoEnabled()) {
            log.info("mq.send|exchange={}, routingKey={}, delayMillis={}, retriedTimes={}, msg={}", exchange, routingKey, delayMillis, retriedTimes, message);
        }

        rabbitMqAdapter.send(exchange, routingKey, message);
    }

    /**
     * 消费失败重试
     *
     * @param rabbitMqAdapter
     * @param object
     * @param headers
     * @param consumerClass
     * @param <T>
     * @return
     */
    public static <T> RetryResult retryAfterConsumerFail(IRabbitMqAdapter rabbitMqAdapter, T object, Map<String, Object> headers, Class<?> consumerClass) {
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

        Object currentRetriedTimes = headers.get(MqConstants.CONSUMER_RETRIED_TIMES);
        int nextRetriedTimes = (currentRetriedTimes == null) ? 1 : ((int) currentRetriedTimes + 1);
        if (nextRetriedTimes > mqConsumerFailRetry.maxRetryTimes()) {
            log.warn("Maximum times[{}] of retries reached", mqConsumerFailRetry.maxRetryTimes());
            return RetryResult.REACHED_RETRY_THRESHOLD;
        }

        // 队列的名称
        String queueName = rabbitListener.queues()[0];
        //延迟交换机名称
        String retryExchangeName = MqNameUtil.getRetryExchangeName(queueName, mqConsumerFailRetry);
        //延迟路由键
        String retryRouteKeyName = MqNameUtil.getRetryRouteKeyName(queueName, mqConsumerFailRetry);
        long retryIntervalSeconds = getRetryIntervalSeconds(mqConsumerFailRetry, nextRetriedTimes);
        send(rabbitMqAdapter, retryExchangeName, retryRouteKeyName, object, nextRetriedTimes, TimeUnit.SECONDS.toMillis(retryIntervalSeconds));
        return RetryResult.SUCCESS;
    }

    /**
     * 获取重试的间隔时间（单位：秒）
     *
     * @param mqConsumerFailRetry
     * @param nextRetriedTimes    nextRetriedTimes从1开始
     * @return
     */
    private static long getRetryIntervalSeconds(MqConsumerFailRetry mqConsumerFailRetry, int nextRetriedTimes) {
        long[] retryIntervalSeconds = mqConsumerFailRetry.retryIntervalSeconds();
        int index = (nextRetriedTimes <= retryIntervalSeconds.length) ? (nextRetriedTimes - 1) : retryIntervalSeconds.length - 1;
        return retryIntervalSeconds[index];
    }

}