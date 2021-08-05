package org.smartframework.cloud.common.web.autoconfigure;

import org.smartframework.cloud.common.web.filter.ReactiveFilter;
import org.smartframework.cloud.starter.configure.SmartAutoConfiguration;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(SmartAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveFilterAutoConfiguration {

    @Bean
    public ReactiveFilter reactiveFilter(final SmartProperties smartProperties) {
        boolean enableRpcProtostuff = smartProperties.getRpc().isProtostuff();
        return new ReactiveFilter(enableRpcProtostuff);
    }

}