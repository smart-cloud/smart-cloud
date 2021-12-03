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
package org.smartframework.cloud.common.web.autoconfigure;

import org.smartframework.cloud.common.web.filter.ServletFilter;
import org.smartframework.cloud.starter.configure.SmartAutoConfiguration;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * servlet过滤器配置
 *
 * @author collin
 * @date 2021-10-31
 */
@Configuration
@AutoConfigureAfter(SmartAutoConfiguration.class)
@ConditionalOnClass(name = {"javax.servlet.Filter"})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletFilterAutoConfiguration {

    @Bean
    public ServletFilter servletFilter(final SmartProperties smartProperties) {
        boolean enableRpcProtostuff = smartProperties.getRpc().isProtostuff();
        return new ServletFilter(enableRpcProtostuff);
    }

    @Bean
    public FilterRegistrationBean<Filter> registServletFilter(final ServletFilter servletFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(servletFilter);
        registration.addUrlPatterns("/*");
        registration.setName(ServletFilter.class.getSimpleName());
        registration.setOrder(OrderConstant.HTTP_FITLER);
        return registration;
    }

}