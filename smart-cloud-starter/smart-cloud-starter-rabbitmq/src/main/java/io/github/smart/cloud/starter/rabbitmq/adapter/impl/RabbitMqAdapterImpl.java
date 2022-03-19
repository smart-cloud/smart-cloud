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
package io.github.smart.cloud.starter.rabbitmq.adapter.impl;

import io.github.smart.cloud.starter.rabbitmq.adapter.IRabbitMqAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * rabbitmq常用接口封装
 *
 * @author collin
 * @2021-12-15
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitMqAdapterImpl implements IRabbitMqAdapter {

    private final RabbitTemplate rabbitTemplate;
    /**
     * 默认的消息最大堆积数
     */
    private static final long DEFAULT_MAX_MESSAGE_COUNT = 128L;

    @Override
    public void send(String exchange, String routingKey, Message message) {
        rabbitTemplate.send(exchange, routingKey, message);
    }

    @Override
    public void checkAndSend(String exchange, String routingKey, String queue, Message message) {
        checkAndSend(exchange, routingKey, queue, DEFAULT_MAX_MESSAGE_COUNT, message);
    }

    @Override
    public void checkAndSend(String exchange, String routingKey, String queue, long maxMessageCount, Message message) {
        long currentMessageCount = rabbitTemplate.execute(channel -> channel.messageCount(queue));
        if (currentMessageCount >= maxMessageCount) {
            log.warn("current message count[{}] had greater than maxMessageCount[{}]", currentMessageCount, maxMessageCount);
            return;
        }
        rabbitTemplate.send(exchange, routingKey, message);
    }

    @Override
    public MessageConverter getMessageConverter() {
        return rabbitTemplate.getMessageConverter();
    }

}