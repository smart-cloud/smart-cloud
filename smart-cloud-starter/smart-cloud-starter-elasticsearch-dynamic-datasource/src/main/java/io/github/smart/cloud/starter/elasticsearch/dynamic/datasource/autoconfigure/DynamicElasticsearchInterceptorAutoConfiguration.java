package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.autoconfigure;

import com.google.common.collect.Lists;
import io.github.smart.cloud.starter.core.business.util.AspectInterceptorUtil;
import io.github.smart.cloud.starter.core.constants.PackageConfig;
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
        String dynamicElasticsearchExpression = AspectInterceptorUtil.buildExpression(PackageConfig.getBasePackages(),
                AspectInterceptorUtil.getMethodExpression(Lists.newArrayList(ES.class)));
        AspectJExpressionPointcut dynamicElasticsearchPointcut = new AspectJExpressionPointcut();
        dynamicElasticsearchPointcut.setExpression(dynamicElasticsearchExpression);

        DefaultBeanFactoryPointcutAdvisor dynamicElasticsearchAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        dynamicElasticsearchAdvisor.setAdvice(dynamicElasticsearchInterceptor);
        dynamicElasticsearchAdvisor.setPointcut(dynamicElasticsearchPointcut);
        return dynamicElasticsearchAdvisor;
    }

}