package io.github.smart.cloud.starter.staticdiscovery.autoconfigure;

import io.github.smart.cloud.starter.staticdiscovery.StaticDiscoveryClient;
import io.github.smart.cloud.starter.staticdiscovery.condition.ConditionalOnStaticDiscoveryClient;
import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(ConditionalOnStaticDiscoveryClient.class)
@AutoConfigureBefore(CompositeDiscoveryClientAutoConfiguration.class)
public class StaticDiscoveryClientAutoConfiguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = StaticDiscoveryProperties.PREFIX)
    public StaticDiscoveryProperties staticDiscoveryProperties() {
        return new StaticDiscoveryProperties();
    }

    @Bean
    @RefreshScope
    public StaticDiscoveryClient staticDiscoveryClient(final StaticDiscoveryProperties staticDiscoveryProperties) {
        return new StaticDiscoveryClient(staticDiscoveryProperties);
    }

}