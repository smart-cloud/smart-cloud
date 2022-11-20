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
package io.github.smart.cloud.starter.web.autoconfigure;

import io.github.smart.cloud.starter.configure.constants.SmartConstant;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.web.aspect.interceptor.ServletApiLogInterceptor;
import io.github.smart.cloud.starter.web.aspect.pointcut.ApiLogPointCut;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * api切面配置
 *
 * @author collin
 * @date 2019-07-03
 */
@Configuration
@ConditionalOnProperty(name = SmartConstant.API_LOG_CONDITION_PROPERTY, havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(name = {"javax.servlet.Filter"})
public class ApiLogAspectAutoConfiguration {

    @Bean
    public ApiLogPointCut apiLogPointCut() {
        return new ApiLogPointCut();
    }

    @Bean
    public ServletApiLogInterceptor apiLogInterceptor(final SmartProperties smartProperties) {
        return new ServletApiLogInterceptor(smartProperties.getApiLog());
    }

    /**
     * api日志切面
     *
     * @param apiLogInterceptor
     * @param apiLogPointCut
     * @return
     */
    @Bean
    public Advisor apiLogAdvisor(final ServletApiLogInterceptor apiLogInterceptor, final ApiLogPointCut apiLogPointCut) {
        DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiLogAdvisor.setAdvice(apiLogInterceptor);
        apiLogAdvisor.setPointcut(apiLogPointCut);

        return apiLogAdvisor;
    }

}