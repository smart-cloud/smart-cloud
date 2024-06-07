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
package io.github.smart.cloud.starter.monitor.api.autoconfigure;

import io.github.smart.cloud.starter.monitor.api.interceptor.ApiMonitorInterceptor;
import io.github.smart.cloud.starter.monitor.api.pointcut.ApiMonitorPointCut;
import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import io.github.smart.cloud.starter.monitor.api.component.ApiMonitorRepository;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口健康检测拦截器配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
public class ApiMonitorInterceptorAutoConfiguration {

    @Bean
    @RefreshScope
    public ApiMonitorRepository apiHealthRepository(final ApiMonitorProperties apiMonitorProperties) {
        return new ApiMonitorRepository(apiMonitorProperties);
    }

    @Bean
    public ApiMonitorPointCut apiHealthPointCut() {
        return new ApiMonitorPointCut();
    }

    @Bean
    public ApiMonitorInterceptor apiHealthInterceptor(final ApiMonitorRepository apiMonitorRepository) {
        return new ApiMonitorInterceptor(apiMonitorRepository);
    }

    @Bean
    public Advisor apiMonitorAdvisor(final ApiMonitorInterceptor apiMonitorInterceptor, final ApiMonitorPointCut apiMonitorPointCut) {
        DefaultBeanFactoryPointcutAdvisor apiMonitorAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiMonitorAdvisor.setAdvice(apiMonitorInterceptor);
        apiMonitorAdvisor.setPointcut(apiMonitorPointCut);

        return apiMonitorAdvisor;
    }

}