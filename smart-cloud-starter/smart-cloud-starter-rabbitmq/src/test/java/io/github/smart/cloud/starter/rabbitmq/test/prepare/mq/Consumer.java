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
package io.github.smart.cloud.starter.rabbitmq.test.prepare.mq;

import io.github.smart.cloud.starter.rabbitmq.test.prepare.constants.MqConstant;
import io.github.smart.cloud.starter.rabbitmq.AbstractRabbitMqConsumer;
import io.github.smart.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@MqConsumerFailRetry(maxRetryTimes = 2)
@RabbitListener(queues = MqConstant.SendCoupon.QUEUE)
public class Consumer extends AbstractRabbitMqConsumer<Long> {

    @Override
    protected void doProcess(Long id) {
        String name = null;
        // 使其抛异常，然后重试
        name.split(",");
    }

}
