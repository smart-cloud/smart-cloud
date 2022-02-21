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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * rabbitmq常用接口封装
 *
 * @author collin
 * @2021-12-15
 */
@Getter
@AllArgsConstructor
public class RabbitMqAdapterImpl implements IRabbitMqAdapter {

    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(String exchange, String routingKey, Message message) throws AmqpException {
        rabbitTemplate.send(exchange, routingKey, message);
    }

}