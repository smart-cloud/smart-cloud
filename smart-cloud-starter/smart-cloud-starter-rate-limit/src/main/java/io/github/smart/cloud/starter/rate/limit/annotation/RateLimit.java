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
package io.github.smart.cloud.starter.rate.limit.annotation;

import io.github.smart.cloud.constants.CommonReturnCodes;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author collin
 * @date 2023-03-19
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 信号量限流大小
     *
     * @return
     */
    int permits();

    /**
     * 限流触发时的提示信息
     *
     * @return
     */
    String message() default CommonReturnCodes.API_ACCESS_TOO_FREQUENT;

}