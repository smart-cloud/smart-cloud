package org.smartframework.cloud.starter.web.autoconfigure;

import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.web.aspect.interceptor.ApiLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(name = {"javax.servlet.Filter"})
public class ApiAspectAutoConfigure {

    /**
     * api切面生效条件
     */
    public static final String API_ASPECT_CONDITION = "${" + SmartConstant.API_LOG_CONDITION_PROPERTY + ":false}";

    @Bean
    public AspectJExpressionPointcut apiPointcut() {
        AspectJExpressionPointcut apiPointcut = new AspectJExpressionPointcut();
        String logExpression = AspectInterceptorUtil.getApiExpression(PackageConfig.getBasePackages());
        apiPointcut.setExpression(logExpression);
        return apiPointcut;
    }

    /**
     * 接口日志
     *
     * @author liyulin
     * @date 2019年7月3日 下午3:58:27
     */
    @Configuration
    @ConditionalOnProperty(name = SmartConstant.API_LOG_CONDITION_PROPERTY, havingValue = "true")
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