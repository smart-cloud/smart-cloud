package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.autoconfigure;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.annotation.ES;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.interceptor.DynamicElasticsearchInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticsearch动态数据源拦截器自动配置
 *
 * @author collin
 * @date 2022-06-03
 */
@Configuration
public class DynamicElasticsearchInterceptorAutoConfiguration {

    @Bean
    public DynamicElasticsearchInterceptor dynamicElasticsearchInterceptor() {
        return new DynamicElasticsearchInterceptor();
    }

    @Bean
    public Advisor dynamicElasticsearchAdvisor(final DynamicElasticsearchInterceptor dynamicElasticsearchInterceptor) {
        AspectJExpressionPointcut dynamicElasticsearchPointcut = new AspectJExpressionPointcut();
        dynamicElasticsearchPointcut.setExpression(String.format("@annotation(%s)", ES.class.getName()));

        DefaultBeanFactoryPointcutAdvisor dynamicElasticsearchAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        dynamicElasticsearchAdvisor.setAdvice(dynamicElasticsearchInterceptor);
        dynamicElasticsearchAdvisor.setPointcut(dynamicElasticsearchPointcut);
        return dynamicElasticsearchAdvisor;
    }

}