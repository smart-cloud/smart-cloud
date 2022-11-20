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
package io.github.smart.cloud.starter.core.method.log.autoconfigure;

import io.github.smart.cloud.starter.configure.constants.SmartConstant;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.core.method.log.annotation.MethodLog;
import io.github.smart.cloud.starter.core.method.log.intercept.MethodLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * method日志切面配置
 *
 * @author collin
 * @date 2020-03-14
 */
@Configuration
@ConditionalOnProperty(name = SmartConstant.METHOD_LOG_CONDITION_PROPERTY, havingValue = "true", matchIfMissing = true)
public class MethodLogInterceptorAutoConfiguration {

    @Bean
    public MethodLogInterceptor methodLogInterceptor(final SmartProperties smartProperties) {
        return new MethodLogInterceptor(smartProperties.getMethodLog());
    }

    @Bean
    public Pointcut methodPointcut() {
        AspectJExpressionPointcut methodLogPointcut = new AspectJExpressionPointcut();
        methodLogPointcut.setExpression(String.format("@annotation(%s)", MethodLog.class.getTypeName()));
        return methodLogPointcut;
    }

    @Bean
    public Advisor methodLogAdvisor(final MethodLogInterceptor methodLogInterceptor, final Pointcut methodPointcut) {
        DefaultBeanFactoryPointcutAdvisor methodLogPointcutAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        methodLogPointcutAdvisor.setAdvice(methodLogInterceptor);
        methodLogPointcutAdvisor.setPointcut(methodPointcut);
        return methodLogPointcutAdvisor;
    }

}