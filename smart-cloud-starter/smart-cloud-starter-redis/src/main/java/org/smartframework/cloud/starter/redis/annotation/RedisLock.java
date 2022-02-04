package org.smartframework.cloud.starter.redis.annotation;

import org.smartframework.cloud.starter.redis.constants.RedisLockConstants;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁注解
 *
 * @author collin
 * @date 2022-02-02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {

    /**
     * 锁的key前缀（默认为方法名）
     *
     * @return
     */
    String keyPrefix() default "";

    /**
     * 锁的key片段
     *
     * @return
     */
    String[] expressions();

    /**
     * 获取锁失败后的提示code
     *
     * @return
     */
    String acquiredFailCode() default RedisLockConstants.ACQUIRED_FAIL_CODE;

    /**
     * 获取锁最大等待时间
     *
     * @return
     */
    long waitTime() default RedisLockConstants.DEFAULT_WAIT_TIME;

    /**
     * 成功获取锁后，锁的释放时间
     *
     * @return
     */
    long leaseTime() default RedisLockConstants.DEFAULT_LEASE_TIME;

    /**
     * 时间单位
     *
     * @return
     */
    TimeUnit unit() default TimeUnit.MICROSECONDS;

}