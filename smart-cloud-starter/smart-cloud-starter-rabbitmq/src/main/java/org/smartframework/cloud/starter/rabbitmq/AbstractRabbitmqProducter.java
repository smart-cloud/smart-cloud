package org.smartframework.cloud.starter.rabbitmq;

import org.smartframework.cloud.starter.rabbitmq.model.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mq发送消息公共封装
 *
 * @author liyulin
 * @date 2020-08-13
 */
@Slf4j
public abstract class AbstractRabbitmqProducter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    protected <T> void send(String exchange, String routingKey, T body) {
        MessageWrapper messageWrapper = new MessageWrapper(JacksonUtil.toJson(body));
        log.info("mq.send|exchange={}, routingKey={}, msg={}", exchange, routingKey, messageWrapper);
        rabbitTemplate.send(exchange, routingKey, messageWrapper.toAmqpMessage());
    }

}