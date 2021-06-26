package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private SmartProperties smartProperties;
    private boolean isInfoEnabled = log.isInfoEnabled();

    protected abstract void doProcess(T t);

    @RabbitHandler
    public void consumer(@Payload byte[] bytes, @Headers Map<String, Object> headers) {
        Message message = MessageBuilder.withBody(bytes).copyHeaders(headers).build();
        UUID messageId = message.getMessageProperties().getHeader(MQConstants.MESSAGE_ID_NAME);
        RLock lock = redissonClient.getLock(MQConstants.IDE_CKECK_LOCK_NAME_PREFIX + messageId);
        // 加锁状态（true：成功；false失败）
        boolean lockState = false;
        // 执行状态（true：成功；false失败）
        boolean executeState = true;
        try {
            lockState = lock.tryLock();
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (lockState) {
                if (isInfoEnabled) {
                    log.info("receive.msg={}", msg);
                }
                doProcess((T) JacksonUtil.parseObject(msg, getBodyClass()));
            } else {
                log.warn("idempotent.check.fail|msg={}", msg);
            }
        } catch (Exception e) {
            executeState = false;
            log.error("consumer mq exception", e);
            throw e;
        } finally {
            if (lockState) {
                if (executeState) {
                    // mq成功消费完后，将已消费标志缓存redis 1小时，做幂等校验
                    lock.lock(getIdempotentCheckCacheMilliSeconds(), TimeUnit.MILLISECONDS);
                } else {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 获取幂等校验缓存有效期
     *
     * @return
     */
    protected long getIdempotentCheckCacheMilliSeconds() {
        // 此处不直接取而是提供一个方法，主要是为了在子类中可以override，以便使某些场景下可以自定义有效期
        return smartProperties.getMq().getIdempotentCheckCacheExpire();
    }

    private Class<T> getBodyClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();

        return (Class<T>) pt.getActualTypeArguments()[0];
    }

}