package org.smartframework.cloud.starter.core.business.autoconfigure;

import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.business.filter.ReactiveFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveFilterAutoConfigure {

    @Bean
    public ReactiveFilter reactiveFilter(final SmartProperties smartProperties) {
        boolean enableRpcProtostuff = smartProperties.getRpc().isProtostuff();
        return new ReactiveFilter(enableRpcProtostuff);
    }

}