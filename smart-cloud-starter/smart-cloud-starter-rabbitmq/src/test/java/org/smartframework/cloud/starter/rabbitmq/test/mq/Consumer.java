package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.smartframework.cloud.starter.rabbitmq.AbstractRabbitMQConsumer;
import org.smartframework.cloud.starter.rabbitmq.annotation.MQConsumerFailRetry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@MQConsumerFailRetry(maxRetryTimes = 2)
@RabbitListener(queues = MQConstant.SendCoupon.QUEUE)
public class Consumer extends AbstractRabbitMQConsumer<Long> {

    @Override
    protected void doProcess(Long id) {
        String name = null;
        // 使其抛异常，然后重试
        name.split(",");
    }

}
