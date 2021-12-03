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
package org.smartframework.cloud.starter.rpc.dubbo.autoconfigure;

import org.apache.dubbo.config.annotation.DubboService;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.rpc.dubbo.interceptor.DubboLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * dubbo切面配置
 *
 * @author liyulin
 * @date 2019-09-19
 */
@Configuration
@ConditionalOnProperty(name = "smart.aspect.dubbolog", havingValue = "true")
public class DubboAspectAutoConfiguration {

    /**
     * dubbo切面
     *
     * @return
     */
    @Bean
    public AspectJExpressionPointcut dubboServicePointcut() {
        AspectJExpressionPointcut dubboServicePointcut = new AspectJExpressionPointcut();

        String dubboExpression = AspectInterceptorUtil.getFinalExpression(PackageConfig.getBasePackages(), AspectInterceptorUtil
                .getTypeExpression(Arrays.asList(DubboService.class)));
        dubboServicePointcut.setExpression(dubboExpression);
        return dubboServicePointcut;
    }

    @Bean
    public DubboLogInterceptor dubboLogInterceptor(final SmartProperties smartProperties) {
        return new DubboLogInterceptor(smartProperties.getLog());
    }

    @Bean
    public Advisor dubboLogAdvisor(final DubboLogInterceptor dubboLogInterceptor,
                                   final AspectJExpressionPointcut dubboServicePointcut) {
        DefaultBeanFactoryPointcutAdvisor dubboAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        dubboAdvisor.setAdvice(dubboLogInterceptor);
        dubboAdvisor.setPointcut(dubboServicePointcut);

        return dubboAdvisor;
    }

}