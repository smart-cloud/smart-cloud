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
package org.smartframework.cloud.starter.rpc.feign.annotation;

import org.smartframework.cloud.starter.rpc.feign.condition.SmartFeignClientCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <code>FeignClient</code>自定义条件封装
 *
 * @author collin
 * @date 2019-03-22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@FeignClient
@Conditional(SmartFeignClientCondition.class)
public @interface SmartFeignClient {

    @AliasFor(annotation = FeignClient.class)
    String name() default "";

    @AliasFor(annotation = FeignClient.class)
    String url() default "";

    @AliasFor(annotation = FeignClient.class)
    String contextId() default "";

    @AliasFor(annotation = FeignClient.class)
    Class<?> fallback() default void.class;

}