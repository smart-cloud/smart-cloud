package org.smartframework.cloud.starter.redis.constants;

import org.smartframework.cloud.starter.redis.annotation.RedisLock;

/**
 * {@link RedisLock}常量
 *
 * @author collin
 * @date 2022-02-04
 */
public interface RedisLockConstants {

    /**
     * key前缀默认值
     */
    String DEFAULT_KEY_PREFIX = "lock:";

    /**
     * 获取锁失败的code
     */
    // TODO:CommonReturnCodes.GET_LOCK_FAIL
    String ACQUIRED_FAIL_CODE = "417";

    /**
     * 默认的获取锁等待时间（5000毫秒）
     */
    long DEFAULT_WAIT_TIME = 5000L;

    /**
     * 默认的锁释放时间，-1表示直到代码执行完毕才释放锁
     */
    long DEFAULT_LEASE_TIME = -1L;

}