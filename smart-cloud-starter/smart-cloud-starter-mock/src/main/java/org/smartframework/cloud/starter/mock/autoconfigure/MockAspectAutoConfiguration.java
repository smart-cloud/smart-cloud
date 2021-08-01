package org.smartframework.cloud.starter.mock.autoconfigure;

import org.smartframework.cloud.starter.configure.properties.MockProperties;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.mock.annotation.Mock;
import org.smartframework.cloud.starter.mock.interceptor.MockInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ConditionalOnProperty(name = "smart.aspect.mock", havingValue = "true")
public class MockAspectAutoConfiguration {

    @Bean
    public MockInterceptor mockInterceptor() {
        return new MockInterceptor();
    }

    @Bean
    public Advisor mockAdvisor(final MockInterceptor mockInterceptor, final SmartProperties smartProperties) {
        MockProperties mockProperties = smartProperties.getMock();
        StringBuilder expression = new StringBuilder();
        if (mockProperties.isApiMock()) {
            expression.append(AspectInterceptorUtil.getTypeExpression(AspectInterceptorUtil.getApiAnnotations()));
        }
        if (mockProperties.isMethodMock()) {
            if (expression.length() > 0) {
                expression.append("(");
                expression.append(expression);
                expression.append(")");
                expression.append("||");
            }
            expression.append(AspectInterceptorUtil.getMethodExpression(Arrays.asList(Mock.class)));
        }

        AspectJExpressionPointcut mockPointcut = new AspectJExpressionPointcut();
        String mockExpression = AspectInterceptorUtil.getFinalExpression(PackageConfig.getBasePackages(), expression.toString());
        mockPointcut.setExpression(mockExpression);

        DefaultBeanFactoryPointcutAdvisor mockAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        mockAdvisor.setAdvice(mockInterceptor);
        mockAdvisor.setPointcut(mockPointcut);

        return mockAdvisor;
    }

}