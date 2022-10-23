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
package io.github.smart.cloud.starter.redis.intercept;

import io.github.smart.cloud.starter.redis.annotation.CacheEvict;
import io.github.smart.cloud.starter.redis.enums.RedisKeyPrefix;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

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

    public RedisEvictInterceptor(RedisTemplate<Object, Object> redisTemplate, RedissonClient redissonClient) {
        super(redisTemplate, redissonClient);
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();

        // 移除缓存
        Method method = invocation.getMethod();
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
        String cacheKey = getKey(RedisKeyPrefix.CACHE.getKey(), cacheEvict.name(), cacheEvict.expressions(), method, invocation.getArguments());
        redisTemplate.delete(cacheKey);
        return result;
    }

}