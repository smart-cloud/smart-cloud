/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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