package io.github.smart.cloud.starter.redis.annotation;

import java.lang.annotation.*;

/**
 * 触发缓存删除操作
 *
 * @author collin
 * @date 2022-03-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheEvict {

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

}