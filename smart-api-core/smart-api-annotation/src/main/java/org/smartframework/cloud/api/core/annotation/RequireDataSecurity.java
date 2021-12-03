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

import org.smartframework.cloud.api.core.annotation.enums.SignType;

import java.lang.annotation.*;

/**
 * 接口数据安全（加解密、签名）注解
 *
 * @author collin
 * @date 2021-03-28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireDataSecurity {

    /**
     * 请求参数是否需要解密
     */
    boolean requestDecrypt() default true;

    /**
     * 响应信息是否需要加密
     */
    boolean responseEncrypt() default true;

    /**
     * 签名控制（默认：请求参数需要验签，且响应参数需要签名）
     */
    SignType sign() default SignType.ALL;

}