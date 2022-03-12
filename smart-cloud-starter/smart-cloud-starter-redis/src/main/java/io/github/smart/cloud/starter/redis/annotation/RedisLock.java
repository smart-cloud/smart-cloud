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
package io.github.smart.cloud.starter.redis.annotation;

import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.redis.constants.RedisLockConstants;

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
    String prefix() default "";

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
    String acquiredFailCode() default CommonReturnCodes.GET_LOCK_FAIL;

    /**
     * 获取锁最大等待时间（默认单位毫秒）
     *
     * @return
     */
    long waitTime() default RedisLockConstants.DEFAULT_WAIT_TIME;

    /**
     * 成功获取锁后，锁的释放时间（默认单位毫秒）
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