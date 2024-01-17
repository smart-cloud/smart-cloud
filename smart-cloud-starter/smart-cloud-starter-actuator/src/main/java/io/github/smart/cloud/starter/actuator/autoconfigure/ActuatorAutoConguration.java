package io.github.smart.cloud.starter.actuator.autoconfigure;

import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * actuator自动配置类
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
public class ActuatorAutoConguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "smart.health")
    public HealthProperties healthProperties() {
        return new HealthProperties();
    }

}