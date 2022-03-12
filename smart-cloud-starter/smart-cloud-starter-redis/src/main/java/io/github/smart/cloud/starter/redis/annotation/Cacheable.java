package io.github.smart.cloud.starter.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存方法的执行结果
 *
 * @author collin
 * @date 2022-03-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cacheable {

    /**
     * 缓存一级分类名称
     *
     * @return
     */
    String name();

    /**
     * 缓存的变量key
     *
     * @return
     */
    String[] expressions();

    /**
     * 缓存有效期
     *
     * @return
     */
    long ttl();

    /**
     * 缓存有效期时间单位
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.SECONDS;

}