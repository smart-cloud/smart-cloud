package org.smartframework.cloud.api.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smartframework.cloud.api.core.enums.SignType;

/**
 * @desc 接口访问控制注解（只支持普通接口，不支持rpc接口）
 * @author liyulin
 * @date 2020/04/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmartApiAC {

	/** 是否需要token校验 */
	boolean tokenCheck() default true;

	/** 签名控制（默认：请求参数需要验签，且响应参数需要签名） */
	SignType sign() default SignType.ALL;

	/** 请求参数是否需要解密 */
	boolean decrypt() default true;

	/** 响应信息是否需要加密 */
	boolean encrypt() default true;

	/** 是否需要权限控制 */
	boolean auth() default false;

	/** 是否需要重复提交校验 */
	boolean repeatSubmitCheck() default false;

}