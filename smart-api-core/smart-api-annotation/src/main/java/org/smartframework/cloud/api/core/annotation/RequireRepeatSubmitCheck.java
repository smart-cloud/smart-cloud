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
package org.smartframework.cloud.api.core.annotation;

import org.smartframework.cloud.api.core.annotation.constants.ApiAnnotationConstants;

import java.lang.annotation.*;

/**
 * 重复提交校验注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRepeatSubmitCheck {

    /**
     * 重复提交校验有效期（如果大于0，表示执行完后expireMillis毫秒内不允许重复提交。单位：毫秒）
     *
     * @return
     */
    long expireMillis() default ApiAnnotationConstants.DEFAULT_EXPIRE_MILLIS_OF_REPEAT_SUBMIT;

}