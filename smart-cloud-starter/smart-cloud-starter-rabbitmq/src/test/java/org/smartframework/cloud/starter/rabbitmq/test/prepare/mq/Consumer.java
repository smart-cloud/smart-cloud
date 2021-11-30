package org.smartframework.cloud.starter.rabbitmq.test.prepare.mq;

import org.smartframework.cloud.starter.rabbitmq.AbstractRabbitMqConsumer;
import org.smartframework.cloud.starter.rabbitmq.annotation.MqConsumerFailRetry;
import org.smartframework.cloud.starter.rabbitmq.test.prepare.constants.MqConstant;
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
