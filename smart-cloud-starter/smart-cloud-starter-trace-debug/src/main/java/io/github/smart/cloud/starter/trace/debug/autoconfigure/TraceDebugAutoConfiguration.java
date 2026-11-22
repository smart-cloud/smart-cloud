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
package io.github.smart.cloud.starter.trace.debug.autoconfigure;

import io.github.smart.cloud.starter.trace.debug.filter.TraceDebugHttpHeaderArgsParseFilter;
import io.github.smart.cloud.starter.trace.debug.intercept.TraceDebugInterceptor;
import io.github.smart.cloud.starter.trace.debug.pointcut.TraceDebugPointCut;
import io.github.smart.cloud.starter.trace.debug.properties.TraceDebugProperties;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 方法级跟踪打印配置
 *
 * @author collin.li
 * @date 2025-12-03
 */
@Configuration
@ConditionalOnProperty(prefix = "smart.trace-debug", name = "enable", havingValue = "true", matchIfMissing = true)
public class TraceDebugAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "smart.trace-debug")
    public TraceDebugProperties traceDebugProperties() {
        return new TraceDebugProperties();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public TraceDebugInterceptor traceDebugInterceptor() {
        return new TraceDebugInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public TraceDebugPointCut traceDebugPointCut(final TraceDebugProperties traceDebugProperties) {
        return new TraceDebugPointCut(traceDebugProperties.getPointCut());
    }

    @Bean
    public DefaultPointcutAdvisor traceAdvisor(final TraceDebugInterceptor traceDebugInterceptor, final TraceDebugPointCut traceDebugPointCut) {
        DefaultPointcutAdvisor traceAdvisor = new DefaultPointcutAdvisor();
        traceAdvisor.setPointcut(traceDebugPointCut);
        traceAdvisor.setAdvice(traceDebugInterceptor);
        return traceAdvisor;
    }

    @Bean
    public TraceDebugHttpHeaderArgsParseFilter traceDebugHttpHeaderArgsParseFilter(final TraceDebugProperties traceDebugProperties) {
        return new TraceDebugHttpHeaderArgsParseFilter(traceDebugProperties);
    }

    @Bean
    public FilterRegistrationBean<TraceDebugHttpHeaderArgsParseFilter> traceDebugHttpHeaderArgsParseFilterRegistration(final TraceDebugHttpHeaderArgsParseFilter traceDebugHttpHeaderArgsParseFilter) {
        FilterRegistrationBean<TraceDebugHttpHeaderArgsParseFilter> traceDebugHttpHeaderArgsParseFilterRegistration = new FilterRegistrationBean<>();
        traceDebugHttpHeaderArgsParseFilterRegistration.setFilter(traceDebugHttpHeaderArgsParseFilter);
        traceDebugHttpHeaderArgsParseFilterRegistration.addUrlPatterns("/*");
        traceDebugHttpHeaderArgsParseFilterRegistration.setName("traceDebugHttpHeaderArgsParseFilter");
        traceDebugHttpHeaderArgsParseFilterRegistration.setOrder(1);
        return traceDebugHttpHeaderArgsParseFilterRegistration;
    }

}