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

import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConverter;

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
     */
    void send(String exchange, String routingKey, Message message);

    /**
     * 检查当前队列大小，满足条件则发送消息
     *
     * @param exchange
     * @param routingKey
     * @param queue
     * @param message
     */
    void checkAndSend(String exchange, String routingKey, String queue, Message message);

    /**
     * 检查当前队列大小，满足条件则发送消息
     *
     * @param exchange
     * @param routingKey
     * @param queue
     * @param maxMessageCount 队列允许的最大消息数，如果超过这个大小，将不再往队列中发送消息
     * @param message
     */
    void checkAndSend(String exchange, String routingKey, String queue, long maxMessageCount, Message message);

    /**
     * 获取消息转换器
     *
     * @return
     */
    MessageConverter getMessageConverter();

}