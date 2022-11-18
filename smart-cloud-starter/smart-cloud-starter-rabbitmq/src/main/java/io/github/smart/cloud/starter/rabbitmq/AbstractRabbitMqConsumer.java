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
import io.github.smart.cloud.utility.security.Md5Util;
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
public abstract class AbstractRabbitMqConsumer<T> implements IRabbitMqConsumer {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private IRabbitMqAdapter rabbitMqAdapter;

    /**
     * 消费者具体执行的业务逻辑
     *
     * @param body
     */
    protected abstract void doProcess(T body);

    /**
     * 重试失败后执行
     *
     * @param body
     * @return
     */
    protected boolean executeAfterRetryConsumerFail(T body) {
        return true;
    }

    /**
     * 获取锁名称（如果不满足，可在子类覆盖默认实现）
     *
     * @param body
     * @return
     */
    protected String getLockName(T body) {
        return MqConstants.IDE_CKECK_LOCK_NAME_PREFIX + Md5Util.md5Hex(JacksonUtil.toBytes(body));
    }

    @RabbitHandler
    public void consumer(@Payload T body, @Headers Map<String, Object> headers) {
        RLock lock = redissonClient.getLock(getLockName(body));
        // 加锁状态（true：成功；false失败）
        boolean isRequiredLock = false;
        try {
            isRequiredLock = lock.tryLock();
            if (isRequiredLock) {
                if (log.isDebugEnabled()) {
                    log.debug("receive.msg={}", JacksonUtil.toJson(body));
                }
                doProcess(body);
            } else {
                log.warn("idempotent.check.fail|msg={}", JacksonUtil.toJson(body));
            }
        } catch (Exception e) {
            log.error("consumer.mq.exception|object={}", JacksonUtil.toJson(body), e);
            RetryResult retryResult = MqUtil.retryAfterConsumerFail(rabbitMqAdapter, body, headers, getClass());
            boolean isThrow = retryResult == RetryResult.NOT_SUPPORT || (retryResult == RetryResult.REACHED_RETRY_THRESHOLD && !executeAfterRetryConsumerFail(body));
            if (isThrow) {
                throw e;
            }
        } finally {
            if (isRequiredLock) {
                lock.unlock();
            }
        }
    }

}