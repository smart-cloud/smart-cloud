package org.smartframework.cloud.starter.web.autoconfigure;

import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.smartframework.cloud.starter.web.aspect.interceptor.ApiIdempotentInterceptor;
import org.smartframework.cloud.starter.web.aspect.interceptor.ApiLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * api切面配置
 * 
 * @author liyulin
 * @date 2019-07-03
 */
@Configuration
@ConditionalOnExpression(ApiAspectAutoConfigure.API_ASPECT_CONDITION)
@ConditionalOnClass(name={"javax.servlet.Filter"})
public class ApiAspectAutoConfigure {

	private static final String API_LOG_CONDITION_PROPERTY = "smart.aspect.apilog";
	private static final String API_IDEMPOTENT_CONDITION_PROPERTY = "smart.aspect.apiidempotent";
	/** api切面生效条件 */
	public static final String API_ASPECT_CONDITION = "${" + API_LOG_CONDITION_PROPERTY + ":false}"
												  + "||${" + API_IDEMPOTENT_CONDITION_PROPERTY + ":false}";

	@Bean
	public AspectJExpressionPointcut apiPointcut() {
		AspectJExpressionPointcut apiPointcut = new AspectJExpressionPointcut();
		String logExpression = AspectInterceptorUtil.getApiExpression(PackageConfig.getBasePackages());
		apiPointcut.setExpression(logExpression);
		return apiPointcut;
	}
	
	/**
	 * 幂等校验
	 * 
	 * @author liyulin
	 * @date 2019年7月3日 下午3:58:27
	 */
	@Configuration
	@ConditionalOnBean(RedisComponent.class)
	@ConditionalOnProperty(name = API_IDEMPOTENT_CONDITION_PROPERTY, havingValue = "true")
	class IdempotentAutoConfigure {

		@Bean
		public ApiIdempotentInterceptor idempotentInterceptor(final RedisComponent redisComponent) {
			return new ApiIdempotentInterceptor(redisComponent);
		}

		/**
		 * api日志切面
		 * 
		 * @param idempotentInterceptor
		 * @param apiPointcut
		 * @return
		 */
		@Bean
		public Advisor idempotentAdvisor(final ApiIdempotentInterceptor idempotentInterceptor,
				final AspectJExpressionPointcut apiPointcut) {
			DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
			apiLogAdvisor.setAdvice(idempotentInterceptor);
			apiLogAdvisor.setPointcut(apiPointcut);

			return apiLogAdvisor;
		}
	}

	/**
	 * 接口日志
	 * 
	 * @author liyulin
	 * @date 2019年7月3日 下午3:58:27
	 */
	@Configuration
	@ConditionalOnProperty(name = API_LOG_CONDITION_PROPERTY, havingValue = "true")
	class ApiLogAutoConfigure {

		@Bean
		public ApiLogInterceptor apiLogInterceptor() {
			return new ApiLogInterceptor();
		}

		/**
		 * api日志切面
		 * 
		 * @param apiLogInterceptor
		 * @param apiPointcut
		 * @return
		 */
		@Bean
		public Advisor apiLogAdvisor(final ApiLogInterceptor apiLogInterceptor,
				final AspectJExpressionPointcut apiPointcut) {
			DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
			apiLogAdvisor.setAdvice(apiLogInterceptor);
			apiLogAdvisor.setPointcut(apiPointcut);

			return apiLogAdvisor;
		}
	}

}