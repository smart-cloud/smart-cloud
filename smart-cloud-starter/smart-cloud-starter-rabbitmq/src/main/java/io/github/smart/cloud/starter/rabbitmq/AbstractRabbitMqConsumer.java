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
package io.github.smart.cloud.starter.rabbitmq;

import io.github.smart.cloud.starter.rabbitmq.adapter.IRabbitMqAdapter;
import io.github.smart.cloud.starter.rabbitmq.util.MqUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
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
 * @author collin
 * @date 2020-08-12
 */
@Slf4j
public abstract class AbstractRabbitMqConsumer<T> implements AbstractRabbitMqConsumerMarker {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IRabbitMqAdapter rabbitMqAdapter;

    /**
     * 消费者具体执行的业务逻辑
     *
     * @param object
     */
    protected abstract void doProcess(T object);

    /**
     * 重试失败后执行
     *
     * @param object
     * @return
     */
    protected boolean executeAfterRetryConsumerFail(T object) {
        return false;
    }

    @RabbitHandler
    public void consumer(@Payload byte[] bytes, @Headers Map<String, Object> headers) {
        Message message = MessageBuilder.withBody(bytes).copyHeaders(headers).build();
        UUID messageId = message.getMessageProperties().getHeader(MqConstants.MESSAGE_ID_NAME);
        RLock lock = redissonClient.getLock(MqConstants.IDE_CKECK_LOCK_NAME_PREFIX + messageId);
        // 加锁状态（true：成功；false失败）
        boolean lockState = false;
        T object = null;
        try {
            lockState = lock.tryLock();
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if (lockState) {
                log.info("receive.msg={}", msg);
                Class<T> tClass = getBodyClass();
                if (tClass == String.class) {
                    object = (T) msg;
                } else {
                    object = JacksonUtil.parseObject(msg, tClass);
                }

                doProcess(object);
            } else {
                log.warn("idempotent.check.fail|msg={}", msg);
            }
        } catch (Exception e) {
            log.error("consumer mq exception", e);
            if (!MqUtil.retryAfterConsumerFail(rabbitMqAdapter, object, message, getClass()) && executeAfterRetryConsumerFail(object)) {
                log.warn("execute fail after retry consumer");
            }
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