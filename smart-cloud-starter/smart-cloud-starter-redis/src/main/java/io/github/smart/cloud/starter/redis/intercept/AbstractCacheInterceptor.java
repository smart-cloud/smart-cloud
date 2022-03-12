package io.github.smart.cloud.starter.redis.intercept;

import io.github.smart.cloud.starter.redis.enums.RedisKeyPrefix;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;

/**
 * 缓存父类
 *
 * @author collin
 * @date 2022-03-11
 */
public abstract class AbstractCacheInterceptor extends AbstractRedisInterceptor {

    public AbstractCacheInterceptor(RedissonClient redissonClient) {
        super(redissonClient);
    }

    /**
     * 获取缓存名称
     *
     * @param name
     * @param method
     * @return
     */
    protected String getCacheName(String name, Method method) {
        return getPrefix(RedisKeyPrefix.CACHE.getKey(), name, method).toString();
    }

    /**
     * 获取缓存key
     *
     * @param expressions
     * @param method
     * @param arguments
     * @return
     */
    protected String getCacheKey(String[] expressions, Method method, Object[] arguments) {
        return getSuffix(expressions, method, arguments).toString();
    }

}