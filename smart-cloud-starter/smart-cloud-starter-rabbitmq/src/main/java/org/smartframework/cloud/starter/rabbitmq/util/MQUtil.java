package org.smartframework.cloud.starter.rabbitmq.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.rabbitmq.MQConstants;
import org.smartframework.cloud.starter.rabbitmq.annotation.MQConsumerFailRetry;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.RetryTimeUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public final class MQUtil {

    /**
     * 发送消息
     *
     * <p>delayTime取值example：String.valueOf(TimeUnit.SECONDS.toMillis(delayMillis))</p>
     *
     * @param rabbitTemplate
     * @param exchange
     * @param routingKey
     * @param object
     * @param retriedTimes
     * @param delayMillis
     * @param <T>
     */
    public static <T> void send(RabbitTemplate rabbitTemplate, String exchange, String routingKey, T object, Integer retriedTimes, String delayMillis) {
        String json = JacksonUtil.toJson(object);
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        MessageBuilder messageBuilder = MessageBuilder.withBody(body);
        if (StringUtils.isNotBlank(delayMillis)) {
            messageBuilder.setExpiration(delayMillis);
        }
        if (retriedTimes != null) {
            messageBuilder.setHeader(MQConstants.CONSUMER_RETRIED_TIMES, retriedTimes);
        }

        if (log.isInfoEnabled()) {
            log.info("mq.send|exchange={}, routingKey={}, delayMillis={}, retriedTimes={}, msg={}", exchange, routingKey, delayMillis, retriedTimes, json);
        }
        rabbitTemplate.send(exchange, routingKey, messageBuilder.build());
    }

    public static <T> void retryAfterConsumerFail(RabbitTemplate rabbitTemplate, T object, Message message, Class<?> consumerClass) {
        if (object == null) {
            return;
        }

        // 失败后，发送延迟消息重试
        MQConsumerFailRetry mqConsumerFailRetry = AnnotationUtils.findAnnotation(consumerClass, MQConsumerFailRetry.class);
        if (mqConsumerFailRetry == null) {
            log.warn("MQConsumerFailRetry not found, retry is skipped!");
            return;
        }
        Integer retriedTimes = message.getMessageProperties().getHeader(MQConstants.CONSUMER_RETRIED_TIMES);
        retriedTimes = (retriedTimes == null) ? 0 : (retriedTimes + 1);
        if (retriedTimes >= mqConsumerFailRetry.maxRetryTimes()) {
            log.warn("Maximum times of retries reached");
            return;
        }

        RabbitListener rabbitListener = AnnotationUtils.findAnnotation(consumerClass, RabbitListener.class);
        if (rabbitListener == null) {
            return;
        }
        // 队列的名称
        String retryQueueName = rabbitListener.queues()[0];
        String retryQueuePrefix = MQNameUtil.getQueuePrefix(retryQueueName);
        //延迟交换机名称
        String retryExchangeName = MQNameUtil.getRetryExchangeName(retryQueuePrefix, mqConsumerFailRetry);
        //延迟路由键
        String delayRouteKeyName = MQNameUtil.getDelayRouteKeyName(retryQueuePrefix, mqConsumerFailRetry);
        send(rabbitTemplate, retryExchangeName, delayRouteKeyName, object, retriedTimes, String.valueOf(TimeUnit.SECONDS.toMillis(RetryTimeUtil.getNextExecuteTime(retriedTimes))));
    }

}