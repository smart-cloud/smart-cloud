package org.smartframework.cloud.starter.rabbitmq.configure;

import org.smartframework.cloud.starter.rabbitmq.components.MqIdeCheckHelper;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liyulin
 * @date 2020-08-12
 */
@Configuration
public class RabbitmqAutoConfiguration {

    @Bean
    public MqIdeCheckHelper ideCheckHelper(final RedisComponent redisComponent) {
        return new MqIdeCheckHelper(redisComponent);
    }

}