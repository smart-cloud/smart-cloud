package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

/**
 * mq发送消息公共封装
 *
 * @author liyulin
 * @date 2020-08-13
 */
@Slf4j
public abstract class AbstractRabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param exchange
     * @param routingKey
     * @param object
     * @param <T>
     */
    protected <T> void send(String exchange, String routingKey, T object) {
        send(exchange, routingKey, object, null);
    }

    /**
     * 发送延迟消息
     *
     * <p>delayTime取值example：String.valueOf(TimeUnit.SECONDS.toMillis(delayMillis))</p>
     *
     * @param exchange
     * @param routingKey
     * @param object
     * @param delayTime
     * @param <T>
     */
    protected <T> void send(String exchange, String routingKey, T object, String delayTime) {
        String json = JacksonUtil.toJson(object);
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        MessageBuilder messageBuilder = MessageBuilder.withBody(body);
        if (StringUtils.isNotBlank(delayTime)) {
            messageBuilder.setExpiration(delayTime);
        }

        if (log.isInfoEnabled()) {
            log.info("mq.send|exchange={}, routingKey={}, delayTime={}, msg={}", exchange, routingKey, delayTime, json);
        }
        rabbitTemplate.send(exchange, routingKey, messageBuilder.build());
    }

}