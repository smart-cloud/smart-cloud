package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {

    @Bean
    public Queue sendCouponQueue() {
        return new Queue(MQConstant.SendCoupon.QUEUE, true);
    }

    @Bean
    public Exchange sendCouponExchange() {
        return ExchangeBuilder.directExchange(MQConstant.SendCoupon.EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding sendCouponBinding() {
        return BindingBuilder.bind(sendCouponQueue()).to(sendCouponExchange()).with(MQConstant.SendCoupon.ROUTING).noargs();
    }

}