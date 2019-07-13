package org.smartframework.cloud.starter.web.autoconfigure;

import org.smartframework.cloud.starter.common.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.common.constants.PackageConfig;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.smartframework.cloud.starter.web.aspect.interceptor.ApiLogInterceptor;
import org.smartframework.cloud.starter.web.aspect.interceptor.ApiSecurityInterceptor;
import org.smartframework.cloud.starter.web.aspect.interceptor.RepeatSubmitCheckInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class ApiAspectAutoConfigure {

	private static final String API_SECURITY_CONDITION_PROPERTY = "smart.aspect.apiSecurity";
	private static final String API_LOG_CONDITION_PROPERTY = "smart.aspect.apilog";
	private static final String REPEAT_SUBMIT_CHECK_CONDITION_PROPERTY = "smart.aspect.repeatSubmitCheck";
	/** api切面生效条件 */
	public static final String API_ASPECT_CONDITION = "${" + API_SECURITY_CONDITION_PROPERTY + ":false}" 
												  + "||${" + API_LOG_CONDITION_PROPERTY + ":false}"
												  + "||${" + REPEAT_SUBMIT_CHECK_CONDITION_PROPERTY + ":false}";

	@Bean
	public AspectJExpressionPointcut apiPointcut() {
		AspectJExpressionPointcut apiPointcut = new AspectJExpressionPointcut();
		String logExpression = AspectInterceptorUtil.getApiExpression(PackageConfig.getBasePackages());
		apiPointcut.setExpression(logExpression);
		return apiPointcut;
	}
	
	/**
	 * 重复提交校验
	 * 
	 * @author liyulin
	 * @date 2019年7月3日 下午3:58:27
	 */
	@Configuration
	@ConditionalOnBean(RedisComponent.class)
	@ConditionalOnProperty(name = REPEAT_SUBMIT_CHECK_CONDITION_PROPERTY, havingValue = "true")
	class RepeatSubmitCheckAutoConfigure {

		@Bean
		public RepeatSubmitCheckInterceptor repeatSubmitCheckInterceptor(final RedisComponent redisComponent) {
			return new RepeatSubmitCheckInterceptor(redisComponent);
		}

		/**
		 * api日志切面
		 * 
		 * @param apiLogInterceptor
		 * @param apiPointcut
		 * @return
		 */
		@Bean
		public Advisor repeatSubmitCheckAdvisor(final RepeatSubmitCheckInterceptor repeatSubmitCheckInterceptor,
				final AspectJExpressionPointcut apiPointcut) {
			DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
			apiLogAdvisor.setAdvice(repeatSubmitCheckInterceptor);
			apiLogAdvisor.setPointcut(apiPointcut);

			return apiLogAdvisor;
		}
	}

	/**
	 * 接口安全处理
	 * 
	 * @author liyulin
	 * @date 2019年7月3日 下午3:58:00
	 */
	@Configuration
	@ConditionalOnBean(RedisComponent.class)
	@ConditionalOnProperty(name = API_SECURITY_CONDITION_PROPERTY, havingValue = "true")
	class ApiSecurityAutoConfigure {

		@Bean
		public ApiSecurityInterceptor apiSecurityInterceptor() {
			return new ApiSecurityInterceptor();
		}

		@Bean
		public Advisor apiSecurityAdvisor(final ApiSecurityInterceptor apiSecurityInterceptor,
				final AspectJExpressionPointcut apiPointcut) {
			DefaultBeanFactoryPointcutAdvisor apiSecurityAdvisor = new DefaultBeanFactoryPointcutAdvisor();
			apiSecurityAdvisor.setAdvice(apiSecurityInterceptor);
			apiSecurityAdvisor.setPointcut(apiPointcut);

			return apiSecurityAdvisor;
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