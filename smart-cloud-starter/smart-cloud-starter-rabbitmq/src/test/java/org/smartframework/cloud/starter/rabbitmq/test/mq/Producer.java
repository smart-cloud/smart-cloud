package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.smartframework.cloud.starter.rabbitmq.AbstractRabbitMQProducer;
import org.springframework.stereotype.Component;

@Component
public class Producer extends AbstractRabbitMQProducer {

    /**
     * 发券
     *
     * @param id
     */
    public void send(Long id) {
        super.send(MQConstant.SendCoupon.EXCHANGE, MQConstant.SendCoupon.ROUTING, id);
    }

}