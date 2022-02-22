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
package io.github.smart.cloud.starter.test.mock.rabbitmq.exchange;

import com.github.fridujo.rabbitmq.mock.AmqArguments;
import com.github.fridujo.rabbitmq.mock.ReceiverRegistry;
import com.github.fridujo.rabbitmq.mock.exchange.MockDirectExchange;
import com.rabbitmq.client.AMQP;
import io.github.smart.cloud.starter.test.mock.rabbitmq.constants.MqConstants;
import org.springframework.amqp.core.MessageProperties;

import java.util.concurrent.TimeUnit;

/**
 * mock 延迟交换机
 *
 * @author collin
 * @date 2022-02-21
 */
public class DelayExchange extends MockDirectExchange {

    public DelayExchange(String name, AmqArguments arguments, ReceiverRegistry receiverRegistry) {
        super(name, arguments, receiverRegistry);
    }

    @Override
    public String getType() {
        return MqConstants.DELAY_MESSAGE_TYPE;
    }

    @Override
    public boolean publish(String previousExchangeName, String routingKey, AMQP.BasicProperties props, byte[] body) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) props.getHeaders().get(MessageProperties.X_DELAY));
        } catch (InterruptedException e) {
        }
        return super.publish(previousExchangeName, routingKey, props, body);
    }

}