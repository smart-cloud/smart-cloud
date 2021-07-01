package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.smartframework.cloud.starter.rabbitmq.util.MQUtil;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * mq监听公共基础封装
 *
 * @author liyulin
 * @date 2020-08-12
 */
@Slf4j
public abstract class AbstractRabbitMQConsumer<T> {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private boolean isInfoEnabled = log.isInfoEnabled();

    protected abstract void doProcess(T t);

    @RabbitHandler
    public void consumer(@Payload byte[] bytes, @Headers Map<String, Object> headers) {
        Message message = MessageBuilder.withBody(bytes).copyHeaders(headers).build();
        UUID messageId = message.getMessageProperties().getHeader(MQConstants.MESSAGE_ID_NAME);
        RLock lock = redissonClient.getLock(MQConstants.IDE_CKECK_LOCK_NAME_PREFIX + messageId);
        // 加锁状态（true：成功；false失败）
        boolean lockState = false;
        T object = null;
        try {
            lockState = lock.tryLock();
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (lockState) {
                if (isInfoEnabled) {
                    log.info("receive.msg={}", msg);
                }
                object = (T) JacksonUtil.parseObject(msg, getBodyClass());
                doProcess(object);
            } else {
                log.warn("idempotent.check.fail|msg={}", msg);
            }
        } catch (Exception e) {
            log.error("consumer mq exception", e);
            MQUtil.retryAfterConsumerFail(rabbitTemplate, object, message, getClass());
        } finally {
            if (lockState) {
                lock.unlock();
            }
        }
    }

    private Class<T> getBodyClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();

        return (Class<T>) pt.getActualTypeArguments()[0];
    }

}