package org.smartframework.cloud.starter.mock.autoconfigure;

import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.mock.interceptor.MockInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "smart.aspect.mock", havingValue = "true")
public class MockAspectAutoConfiguration {

	@Bean
	public MockInterceptor mockInterceptor() {
		return new MockInterceptor();
	}

	@Bean
	public Advisor mockAdvisor(final MockInterceptor mockInterceptor) {
		AspectJExpressionPointcut mockPointcut = new AspectJExpressionPointcut();
		String logExpression = AspectInterceptorUtil.getApiExpression(PackageConfig.getBasePackages());
		mockPointcut.setExpression(logExpression);

		DefaultBeanFactoryPointcutAdvisor mockAdvisor = new DefaultBeanFactoryPointcutAdvisor();
		mockAdvisor.setAdvice(mockInterceptor);
		mockAdvisor.setPointcut(mockPointcut);

		return mockAdvisor;
	}

}