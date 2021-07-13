package org.smartframework.cloud.api.core.user.context.autoconfigure;

import org.smartframework.cloud.api.core.user.context.filter.CleanUserContextReactiveFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CleanUserContextReactiveFilterAutoConfiguration {

    @Bean
    public CleanUserContextReactiveFilter cleanUserContextReactiveFilter() {
        return new CleanUserContextReactiveFilter();
    }

}