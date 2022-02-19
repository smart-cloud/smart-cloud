/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.api.core.user.context.autoconfigure;

import io.github.smart.cloud.api.core.user.context.filter.CleanUserContextServletFilter;
import io.github.smart.cloud.starter.configure.constants.OrderConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 清除用户上下文过滤器配置
 *
 * @author collin
 * @date 2021-10-31
 */
@Configuration
@ConditionalOnClass(name = {"javax.servlet.Filter"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CleanUserContextServletFilterAutoConfiguration {

    @Bean
    public CleanUserContextServletFilter cleanUserContextServletFilter() {
        return new CleanUserContextServletFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> registCleanUserContextServletFilter(final CleanUserContextServletFilter cleanUserContextServletFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(cleanUserContextServletFilter);
        registration.addUrlPatterns("/*");
        registration.setName(CleanUserContextServletFilter.class.getSimpleName());
        registration.setOrder(OrderConstant.CLEAN_USER_CONTEXT_FILTER);
        return registration;
    }

}