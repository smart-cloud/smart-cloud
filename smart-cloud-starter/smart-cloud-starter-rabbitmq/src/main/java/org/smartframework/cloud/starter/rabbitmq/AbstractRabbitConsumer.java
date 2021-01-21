package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.starter.rabbitmq.components.MqIdeCheckHelper;
import org.smartframework.cloud.starter.rabbitmq.model.MessageWrapper;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * mq监听公共基础封装
 *
 * @author liyulin
 * @date 2020-08-12
 */
@Slf4j
public abstract class AbstractRabbitConsumer<T> {

    @Autowired
    private MqIdeCheckHelper ideCheckHelper;

    protected abstract void doProcess(T t);

    @RabbitHandler
    public void consumer(@Payload byte[] body, @Headers Map<String, Object> headers) {
        try {
            Message message = MessageBuilder.withBody(body).copyHeaders(headers).build();
            MessageWrapper messageWrapper = MessageWrapper.convert(message);
            if (!ideCheckHelper.check(messageWrapper.getMsgId())) {
                log.warn("idempotent check fail|msg={}", message);
                return;
            }

            log.info("receive.msg={}", messageWrapper);
            doProcess((T) JacksonUtil.parseObject(messageWrapper.getPayload(), getBodyClass()));
        } catch (Exception e) {
            log.error("consumer mq exception", e);
        }
    }

    private Class<T> getBodyClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();

        return (Class<T>) pt.getActualTypeArguments()[0];
    }

}