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
package io.github.smart.cloud.starter.web.autoconfigure;

import io.github.smart.cloud.starter.web.aspect.ExceptionControllerAdvice;
import io.github.smart.cloud.starter.web.exception.ExceptionHandlerContext;
import io.github.smart.cloud.starter.web.exception.ExceptionHandlerStrategyFactory;
import io.github.smart.cloud.starter.web.exception.strategy.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局异常配置
 *
 * @author collin
 * @date 2024-02-15
 */
@Configuration
public class ExceptionControllerAdviceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BaseExceptionHandlerStrategy baseExceptionHandlerStrategy() {
        return new BaseExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public BindExceptionHandlerStrategy bindExceptionHandlerStrategy() {
        return new BindExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConstraintViolationExceptionHandlerStrategy constraintViolationExceptionHandlerStrategy() {
        return new ConstraintViolationExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public HttpMediaTypeNotSupportedExceptionHandlerStrategy httpMediaTypeNotSupportedExceptionHandlerStrategy() {
        return new HttpMediaTypeNotSupportedExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public HttpRequestMethodNotSupportedExceptionHandlerStrategy httpRequestMethodNotSupportedExceptionHandlerStrategy() {
        return new HttpRequestMethodNotSupportedExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public IllegalArgumentExceptionHandlerStrategy illegalArgumentExceptionHandlerStrategy() {
        return new IllegalArgumentExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public MaxUploadSizeExceededExceptionHandlerStrategy maxUploadSizeExceededExceptionHandlerStrategy() {
        return new MaxUploadSizeExceededExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodArgumentNotValidExceptionHandlerStrategy methodArgumentNotValidExceptionHandlerStrategy() {
        return new MethodArgumentNotValidExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    public MismatchedInputExceptionHandlerStrategy mismatchedInputExceptionHandlerStrategy() {
        return new MismatchedInputExceptionHandlerStrategy();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public NoHandlerFoundExceptionHandlerStrategy noHandlerFoundExceptionHandlerStrategy() {
        return new NoHandlerFoundExceptionHandlerStrategy();
    }


    @Bean
    @ConditionalOnMissingBean
    public ExceptionHandlerStrategyFactory exceptionHandlerStrategyFactory() {
        return new ExceptionHandlerStrategyFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionHandlerContext exceptionHandlerContext(final ExceptionHandlerStrategyFactory exceptionHandlerStrategyFactory) {
        return new ExceptionHandlerContext(exceptionHandlerStrategyFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionControllerAdvice exceptionControllerAdvice(final ExceptionHandlerContext exceptionHandlerContext) {
        return new ExceptionControllerAdvice(exceptionHandlerContext);
    }

}