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