package org.smartframework.cloud.starter.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smartframework.cloud.starter.web.aspect.interceptor.RepeatSubmitCheckInterceptor;

/**
 * 重复提交校验注解，用于请求接口，加注解表示校验
 * <p>实现逻辑见{@link RepeatSubmitCheckInterceptor}
 * 
 * @author liyulin
 * @date 2019-06-13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatReqValidate {

	/** 重复提交的最大间隔时间（默认30秒。单位：毫秒） */
	long expireMillis() default 30000;

	/** 提示消息（自定义消息时，要支持国际化） */
	String message() default "message.smart.repeat.submit";

}