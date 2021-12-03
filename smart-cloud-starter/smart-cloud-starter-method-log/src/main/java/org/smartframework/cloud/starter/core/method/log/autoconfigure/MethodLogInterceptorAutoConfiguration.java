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
package org.smartframework.cloud.starter.core.method.log.autoconfigure;

import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.method.log.annotation.MethodLog;
import org.smartframework.cloud.starter.core.method.log.intercept.MethodLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * method日志切面配置
 *
 * @author collin
 * @date 2020-03-14
 */
@Configuration
public class MethodLogInterceptorAutoConfiguration {

    @Bean
    public MethodLogInterceptor methodLogInterceptor(final SmartProperties smartProperties) {
        return new MethodLogInterceptor(smartProperties.getLog());
    }

    @Bean
    public AspectJExpressionPointcut methodPointcut() {
        AspectJExpressionPointcut apiPointcut = new AspectJExpressionPointcut();
        String methodLogName = MethodLog.class.getTypeName();
        apiPointcut.setExpression(String.format("@annotation(%s)", methodLogName));
        return apiPointcut;
    }

    @Bean
    public Advisor methodLogAdvisor(final MethodLogInterceptor methodLogInterceptor,
                                    final AspectJExpressionPointcut methodPointcut) {
        DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiLogAdvisor.setAdvice(methodLogInterceptor);
        apiLogAdvisor.setPointcut(methodPointcut);

        return apiLogAdvisor;
    }

}