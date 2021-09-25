package org.smartframework.cloud.starter.core.business.autoconfigure;

import org.smartframework.cloud.starter.core.business.log.MethodLog;
import org.smartframework.cloud.starter.core.business.log.MethodLogInterceptor;
import org.smartframework.cloud.utility.spring.condition.ConditionEnableLogInfo;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * method日志切面配置
 *
 * @author collin
 * @date 2020-03-14
 */
@Configuration
@Conditional(ConditionEnableLogInfo.class)
public class MethodLogInterceptorAutoConfiguration {

    @Bean
    public MethodLogInterceptor methodLogInterceptor() {
        return new MethodLogInterceptor();
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