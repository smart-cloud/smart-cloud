package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.interceptor;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.annotation.ES;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicElasticsearchDataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * elasticsearch动态数据源拦截器
 *
 * @author collin
 * @date 2022-06-03
 */
public class DynamicElasticsearchInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ES es = invocation.getMethod().getAnnotation(ES.class);
        String dsKey = es.value();
        DynamicElasticsearchDataSourceContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            DynamicElasticsearchDataSourceContextHolder.poll();
        }
    }

}