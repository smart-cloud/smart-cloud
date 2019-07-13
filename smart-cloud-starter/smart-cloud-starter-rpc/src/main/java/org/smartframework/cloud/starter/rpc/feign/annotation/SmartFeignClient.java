package org.smartframework.cloud.starter.rpc.feign.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smartframework.cloud.starter.rpc.feign.condition.SmartFeignClientCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import springfox.documentation.annotations.ApiIgnore;

/**
 * <code>FeignClient</code>自定义条件封装
 *
 * @author liyulin
 * @date 2019-03-22
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@FeignClient
@Conditional(SmartFeignClientCondition.class)
@ApiIgnore
public @interface SmartFeignClient {

	@AliasFor(annotation = FeignClient.class)
	String name() default "";

	@AliasFor(annotation = FeignClient.class)
	String url() default "";

	@AliasFor(annotation = FeignClient.class)
	Class<?> fallback() default void.class;

}