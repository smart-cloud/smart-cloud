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
package io.github.smart.cloud.starter.monitor.actuator.autoconfigure;

import io.github.smart.cloud.starter.monitor.actuator.interceptor.ApiHealthInterceptor;
import io.github.smart.cloud.starter.monitor.actuator.pointcut.ApiHealthPointCut;
import io.github.smart.cloud.starter.monitor.actuator.properties.HealthProperties;
import io.github.smart.cloud.starter.monitor.actuator.repository.ApiHealthRepository;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口健康检测拦截器配置
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
public class ApiHealthInterceptorAutoConfiguration {

    @Bean
    public ApiHealthRepository apiHealthRepository(final HealthProperties healthProperties) {
        return new ApiHealthRepository(healthProperties);
    }

    @Bean
    public ApiHealthPointCut apiHealthPointCut() {
        return new ApiHealthPointCut();
    }

    @Bean
    public ApiHealthInterceptor apiHealthInterceptor(final ApiHealthRepository apiHealthRepository) {
        return new ApiHealthInterceptor(apiHealthRepository);
    }

    @Bean
    public Advisor apiHealthAdvisor(final ApiHealthInterceptor apiHealthInterceptor, final ApiHealthPointCut apiHealthPointCut) {
        DefaultBeanFactoryPointcutAdvisor apiHealthAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiHealthAdvisor.setAdvice(apiHealthInterceptor);
        apiHealthAdvisor.setPointcut(apiHealthPointCut);

        return apiHealthAdvisor;
    }

}