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
package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.rpc.feign.annotation.SmartFeignClient;
import org.smartframework.cloud.starter.rpc.feign.interceptor.FeignLogInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * feign日志切面配置
 *
 * @author collin
 * @date 2019-07-03
 */
@Configuration
@ConditionalOnExpression(FeignLogAspectAutoConfiguration.FEIGN_ASPECT_CONDITION)
public class FeignLogAspectAutoConfiguration {

    /**
     * rpc切面生效条件
     */
    public static final String FEIGN_ASPECT_CONDITION = "${" + SmartConstant.FEIGN_LOG_CONDITION_PROPERTY + ":false}";

    /**
     * feign切面
     *
     * @return
     */
    @Bean
    public AspectJExpressionPointcut feignClientPointcut() {
        AspectJExpressionPointcut feignClientPointcut = new AspectJExpressionPointcut();

        String feignExpression = AspectInterceptorUtil.getFinalExpression(PackageConfig.getBasePackages(), AspectInterceptorUtil
                .getTypeExpression(Arrays.asList(FeignClient.class, SmartFeignClient.class)));
        feignClientPointcut.setExpression(feignExpression);
        return feignClientPointcut;
    }

    /**
     * feign日志
     *
     * @author collin
     * @date 2019年7月3日 下午3:58:48
     */
    @Configuration
    @ConditionalOnProperty(name = SmartConstant.FEIGN_LOG_CONDITION_PROPERTY, havingValue = "true")
    class FeignLogAutoConfigure {

        @Bean
        public FeignLogInterceptor feignInterceptor(final SmartProperties smartProperties) {
            return new FeignLogInterceptor(smartProperties.getLog());
        }

        @Bean
        public Advisor feignLogAdvisor(final FeignLogInterceptor feignInterceptor,
                                       final AspectJExpressionPointcut feignClientPointcut) {
            DefaultBeanFactoryPointcutAdvisor feignAdvisor = new DefaultBeanFactoryPointcutAdvisor();
            feignAdvisor.setAdvice(feignInterceptor);
            feignAdvisor.setPointcut(feignClientPointcut);

            return feignAdvisor;
        }

    }

}