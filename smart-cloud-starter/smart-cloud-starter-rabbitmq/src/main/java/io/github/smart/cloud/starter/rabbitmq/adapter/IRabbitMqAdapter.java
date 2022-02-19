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
package io.github.smart.cloud.starter.rabbitmq.adapter;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;

/**
 * rabbitmq防腐层
 *
 * @author collin
 * @2021-12-15
 */
public interface IRabbitMqAdapter {

    /**
     * 使用特定的路由键向特定的交换机发送消息
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    要发送的消息
     * @throws AmqpException
     */
    void send(String exchange, String routingKey, Message message) throws AmqpException;

}