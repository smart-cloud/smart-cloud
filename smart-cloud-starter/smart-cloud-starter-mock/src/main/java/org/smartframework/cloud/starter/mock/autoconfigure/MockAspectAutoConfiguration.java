package org.smartframework.cloud.starter.mock.autoconfigure;

import org.smartframework.cloud.starter.configure.SmartAutoConfiguration;
import org.smartframework.cloud.starter.configure.properties.MockProperties;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.mock.annotation.Mock;
import org.smartframework.cloud.starter.mock.condition.MockCondition;
import org.smartframework.cloud.starter.mock.interceptor.MockInterceptor;
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
        String mockExpression = AspectInterceptorUtil.getFinalExpression(PackageConfig.getBasePackages(), AspectInterceptorUtil.getMethodExpression(annotations));
        mockPointcut.setExpression(mockExpression);

        DefaultBeanFactoryPointcutAdvisor mockAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        mockAdvisor.setAdvice(mockInterceptor);
        mockAdvisor.setPointcut(mockPointcut);

        return mockAdvisor;
    }

}