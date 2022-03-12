package io.github.smart.cloud.starter.redis.intercept;

import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.exception.AcquiredLockFailException;
import io.github.smart.cloud.starter.redis.annotation.Cacheable;
import io.github.smart.cloud.starter.redis.constants.RedisLockConstants;
import io.github.smart.cloud.starter.redis.enums.RedisKeyPrefix;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 缓存拦截器
 *
 * @author collin
 * @date 2022-03-11
 * @see Cacheable
 */
public class CacheableInterceptor extends AbstractCacheInterceptor {

    public CacheableInterceptor(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        String cacheName = getCacheName(cacheable.name(), method);
        String cacheKey = getCacheKey(cacheable.expressions(), method, invocation.getArguments());
        // 从缓存获取
        RMapCache<String, Object> mapCache = redissonClient.getMapCache(cacheName);
        Object cache = mapCache.get(cacheKey);
        if (cache != null) {
            return cache;
        }

        // 缓存中没有，则从数据源获取，并放入缓存
        // 为了避免出现缓存雪崩，加分布式锁
        RLock lock = redissonClient.getLock(String.format("%scache", RedisKeyPrefix.LOCK.getKey()));
        boolean isRequiredLock = false;
        try {
            isRequiredLock = lock.tryLock(RedisLockConstants.DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
            if (!isRequiredLock) {
                throw new AcquiredLockFailException(CommonReturnCodes.GET_LOCK_FAIL);
            }
            // 再次从缓存中获取一次，如果存在则返回
            cache = mapCache.get(cacheKey);
            if (cache != null) {
                return cache;
            }

            Object result = invocation.proceed();
            mapCache.put(cacheKey, result, cacheable.ttl(), cacheable.unit());
            return result;
        } finally {
            if (isRequiredLock) {
                lock.unlock();
            }
        }
    }

}