package io.github.smart.cloud.starter.actuator.autoconfigure;

import io.github.smart.cloud.starter.actuator.indicator.ApiHealthIndicator;
import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口健康检测配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
@ConditionalOnEnabledHealthIndicator("api")
public class ApiHealthIndicatorAutoConfiguration {

    @Bean
    public ApiHealthIndicator apiHealthIndicator(final ApiHealthRepository apiHealthRepository) {
        return new ApiHealthIndicator(apiHealthRepository);
    }

}