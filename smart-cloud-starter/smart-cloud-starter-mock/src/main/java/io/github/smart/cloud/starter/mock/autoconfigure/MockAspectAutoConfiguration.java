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
package io.github.smart.cloud.starter.mock.autoconfigure;

import io.github.smart.cloud.starter.configure.SmartAutoConfiguration;
import io.github.smart.cloud.starter.configure.properties.MockProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.core.business.util.AspectInterceptorUtil;
import io.github.smart.cloud.starter.core.constants.PackageConfig;
import io.github.smart.cloud.starter.mock.interceptor.MockInterceptor;
import io.github.smart.cloud.starter.mock.annotation.Mock;
import io.github.smart.cloud.starter.mock.condition.MockCondition;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * mock切面配置
 *
 * @author collin
 * @date 2021-11-13
 */
@Configuration
@AutoConfigureAfter(SmartAutoConfiguration.class)
@Conditional(MockCondition.class)
public class MockAspectAutoConfiguration {

    @Bean
    public MockInterceptor mockInterceptor(final SmartProperties smartProperties) {
        return new MockInterceptor(smartProperties.getMock());
    }

    @Bean
    public Advisor mockAdvisor(final MockInterceptor mockInterceptor, final SmartProperties smartProperties) {
        MockProperties mockProperties = smartProperties.getMock();
        List<Class<? extends Annotation>> annotations = new ArrayList<>(8);
        if (mockProperties.isApi()) {
            annotations.addAll(AspectInterceptorUtil.getApiAnnotations());
        }
        if (mockProperties.isMethod()) {
            annotations.add(Mock.class);
        }

        if (annotations.isEmpty()) {
            throw new UnsupportedOperationException("api and method mock cannot be false at the same time!");
        }

        AspectJExpressionPointcut mockPointcut = new AspectJExpressionPointcut();
        String mockExpression = AspectInterceptorUtil.buildExpression(PackageConfig.getBasePackages(), AspectInterceptorUtil.getMethodExpression(annotations));
        mockPointcut.setExpression(mockExpression);

        DefaultBeanFactoryPointcutAdvisor mockAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        mockAdvisor.setAdvice(mockInterceptor);
        mockAdvisor.setPointcut(mockPointcut);

        return mockAdvisor;
    }

}