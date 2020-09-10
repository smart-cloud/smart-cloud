package org.smartframework.cloud.starter.rpc.feign.annotation;

import org.smartframework.cloud.starter.rpc.feign.condition.SmartFeignClientCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <code>FeignClient</code>自定义条件封装
 *
 * @author liyulin
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