package org.smartframework.cloud.starter.rabbitmq.test.prepare.mq;

import org.smartframework.cloud.starter.rabbitmq.AbstractRabbitMqProducer;
import org.smartframework.cloud.starter.rabbitmq.test.prepare.constants.MqConstant;
import org.springframework.stereotype.Component;

@Component
public class Producer extends AbstractRabbitMqProducer {

    /**
     * 发券
     *
     * @param id
     */
    public void send(Long id) {
        super.send(MqConstant.SendCoupon.EXCHANGE, MqConstant.SendCoupon.ROUTING, id);
    }

}