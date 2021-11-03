package org.smartframework.cloud.starter.rabbitmq.test.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfiguration {

    @Bean
    public Queue sendCouponQueue() {
        return new Queue(MqConstant.SendCoupon.QUEUE, true);
    }

    @Bean
    public Exchange sendCouponExchange() {
        return ExchangeBuilder.directExchange(MqConstant.SendCoupon.EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding sendCouponBinding() {
        return BindingBuilder.bind(sendCouponQueue()).to(sendCouponExchange()).with(MqConstant.SendCoupon.ROUTING).noargs();
    }

}