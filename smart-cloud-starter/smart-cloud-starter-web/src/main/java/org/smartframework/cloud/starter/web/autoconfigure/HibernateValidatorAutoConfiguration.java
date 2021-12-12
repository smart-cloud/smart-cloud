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
package org.smartframework.cloud.starter.web.autoconfigure;

import org.smartframework.cloud.starter.web.validation.ValidatorSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validator;

/**
 * Hibernate Validator校验配置
 *
 * @author collin
 * @date 2019-03-29
 */
@Configuration
public class HibernateValidatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MethodValidationPostProcessor methodValidationPostProcessor(final Validator validator) {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        /** 设置validator模式为快速失败返回 */
        postProcessor.setValidator(ValidatorSingleton.getInstance());
        return postProcessor;
    }

}