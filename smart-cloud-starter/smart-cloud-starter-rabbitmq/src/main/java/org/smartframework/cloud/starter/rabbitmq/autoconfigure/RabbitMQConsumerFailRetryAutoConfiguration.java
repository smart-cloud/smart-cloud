package org.smartframework.cloud.starter.rabbitmq.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.starter.rabbitmq.RabbitMQConsumerFailRetryBeanProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq消费失败重试配置
 *
 * @author collin
 * @date 2021-06-30
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RabbitAutoConfiguration.class)
public class RabbitMQConsumerFailRetryAutoConfiguration {

    @Bean
    public RabbitMQConsumerFailRetryBeanProcessor rabbitMQConsumerFailRetryBeanProcessor() {
        return new RabbitMQConsumerFailRetryBeanProcessor();
    }

}