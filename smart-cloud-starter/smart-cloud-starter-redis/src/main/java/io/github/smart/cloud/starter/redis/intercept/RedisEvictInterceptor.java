package io.github.smart.cloud.starter.redis.intercept;

import io.github.smart.cloud.starter.redis.annotation.CacheEvict;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RedissonClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * 缓存移除拦截器
 *
 * @author collin
 * @date 2022-03-11
 * @see CacheEvict
 */
public class RedisEvictInterceptor extends AbstractCacheInterceptor {

    public RedisEvictInterceptor(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();

        // 移除缓存
        Method method = invocation.getMethod();
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
        redissonClient.getMapCache(getCacheName(cacheEvict.name(), method))
                .remove(getCacheKey(cacheEvict.expressions(), method, invocation.getArguments()));

        return result;
    }

}