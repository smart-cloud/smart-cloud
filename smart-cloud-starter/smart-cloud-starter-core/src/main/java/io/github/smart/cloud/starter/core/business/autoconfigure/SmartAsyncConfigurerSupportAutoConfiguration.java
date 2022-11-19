package io.github.smart.cloud.starter.core.business.autoconfigure;

import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.core.business.SmartAsyncConfigurerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步配置
 *
 * @author collin
 * @date 2021-10-31
 */
@EnableAsync
@Configuration
@ConditionalOnProperty(prefix = "smart.async", name = "enable", havingValue = "true", matchIfMissing = true)
public class SmartAsyncConfigurerSupportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartAsyncConfigurerSupport smartAsyncConfigurerSupport(final SmartProperties smartProperties) {
        return new SmartAsyncConfigurerSupport(smartProperties.getAsync());
    }

}