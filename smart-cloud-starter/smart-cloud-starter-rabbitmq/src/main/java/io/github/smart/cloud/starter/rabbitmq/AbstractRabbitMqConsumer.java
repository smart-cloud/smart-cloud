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
import io.github.smart.cloud.starter.rabbitmq.enums.RetryResult;
import io.github.smart.cloud.starter.rabbitmq.util.MqUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

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
        return true;
    }

    @RabbitHandler
    public void consumer(@Payload T object, @Headers Map<String, Object> headers) {
        Object messageId = headers.get(MqConstants.MESSAGE_ID_NAME);
        RLock lock = redissonClient.getLock(MqConstants.IDE_CKECK_LOCK_NAME_PREFIX + messageId);
        // 加锁状态（true：成功；false失败）
        boolean isRequiredLock = false;
        try {
            isRequiredLock = lock.tryLock();
            if (isRequiredLock) {
                if (log.isDebugEnabled()) {
                    log.debug("receive.msg={}", JacksonUtil.toJson(object));
                }
                doProcess(object);
            } else {
                log.warn("idempotent.check.fail|msg={}", JacksonUtil.toJson(object));
            }
        } catch (Exception e) {
            log.error("consumer.mq.exception|object={}", JacksonUtil.toJson(object), e);
            RetryResult retryResult = MqUtil.retryAfterConsumerFail(rabbitMqAdapter, object, headers, getClass());
            if (retryResult == RetryResult.NOT_SUPPORT) {
                throw e;
            } else if (retryResult == RetryResult.REACHED_RETRY_THRESHOLD && !executeAfterRetryConsumerFail(object)) {
                throw e;
            }
        } finally {
            if (isRequiredLock) {
                lock.unlock();
            }
        }
    }

}