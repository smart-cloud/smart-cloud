package org.smartframework.cloud.api.core.user.context.autoconfigure;

import org.smartframework.cloud.api.core.user.context.filter.CleanUserContextReactiveFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 清除用户上下文过滤器配置
 *
 * @author collin
 * @date 2021-10-31
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CleanUserContextReactiveFilterAutoConfiguration {

    @Bean
    public CleanUserContextReactiveFilter cleanUserContextReactiveFilter() {
        return new CleanUserContextReactiveFilter();
    }

}