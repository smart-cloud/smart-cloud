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
package io.github.smart.cloud.starter.test.mock.rabbitmq.autoconfigure;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.github.fridujo.rabbitmq.mock.exchange.MockExchangeCreator;
import io.github.smart.cloud.starter.test.mock.rabbitmq.constants.MqConstants;
import io.github.smart.cloud.starter.test.mock.rabbitmq.exchange.DelayExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq mock配置
 *
 * @author collin
 * @date 2022-02-22
 */
@Configuration
public class MqMockAutoConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        MockConnectionFactory mockConnectionFactory = new MockConnectionFactory();
        mockConnectionFactory.withAdditionalExchange(MockExchangeCreator.creatorWithExchangeType(MqConstants.DELAY_MESSAGE_TYPE, DelayExchange::new));
        return new CachingConnectionFactory(mockConnectionFactory);
    }

}