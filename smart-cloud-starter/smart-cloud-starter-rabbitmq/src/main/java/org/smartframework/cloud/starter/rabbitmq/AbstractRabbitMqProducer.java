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
package org.smartframework.cloud.starter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.starter.rabbitmq.util.MqUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mq发送消息公共封装
 *
 * @author liyulin
 * @date 2020-08-13
 */
@Slf4j
public abstract class AbstractRabbitMqProducer {

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
     * @param delayMillis 延迟时间（单位：毫秒）
     * @param <T>
     */
    protected <T> void send(String exchange, String routingKey, T object, String delayMillis) {
        MqUtil.send(rabbitTemplate, exchange, routingKey, object, null, delayMillis);
    }

}