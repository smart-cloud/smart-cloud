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
package io.github.smart.cloud.starter.redis.test.integration;

import io.github.smart.cloud.starter.redis.constants.NullCacheValue;
import io.github.smart.cloud.starter.redis.enums.RedisKeyPrefix;
import io.github.smart.cloud.starter.redis.test.prepare.bo.CreateOrderBO;
import io.github.smart.cloud.starter.redis.test.prepare.dataobject.OrderInfo;
import io.github.smart.cloud.starter.redis.test.prepare.service.ICacheTestService;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

class CacheIntegrationTest extends AbstractRedisIntegrationTest {

    @Autowired
    private ICacheTestService cacheTestService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    void testCacheable() {
        String orderNo = RandomStringUtils.random(32, true, true);
        OrderInfo orderInfo = cacheTestService.query(orderNo);
        Assertions.assertThat(orderInfo).isNotNull();

        String key = RedisKeyPrefix.CACHE.getKey() + "order:" + orderNo;
        OrderInfo orderInfoCache = (OrderInfo) redisTemplate.opsForValue().get(key);

        Assertions.assertThat(orderInfoCache).isNotNull();
        Assertions.assertThat(orderInfoCache.getOrderNo()).isNotBlank();
        Assertions.assertThat(orderInfoCache.getOrderNo()).isEqualTo(orderInfoCache.getOrderNo());
        Assertions.assertThat(orderInfoCache.getPrice()).isNotNull();
        Assertions.assertThat(orderInfoCache.getPrice()).isEqualTo(orderInfoCache.getPrice());
    }

    @Test
    void testNullResultIsNotCachedByDefault() {
        String key = RandomStringUtils.random(32, true, true);
        int invocationCount = cacheTestService.getNullQueryInvocationCount();

        Assertions.assertThat(cacheTestService.queryNullWithoutCache(key)).isNull();

        String cacheKey = cacheKey("null-without-cache", key);
        Assertions.assertThat(redisTemplate.opsForValue().get(cacheKey)).isNull();

        Assertions.assertThat(cacheTestService.queryNullWithoutCache(key)).isNull();
        Assertions.assertThat(cacheTestService.getNullQueryInvocationCount()).isEqualTo(invocationCount + 2);
    }

    @Test
    void testNullResultUsesDefaultTtlAndIsCached() {
        String key = RandomStringUtils.random(32, true, true);
        int invocationCount = cacheTestService.getNullQueryInvocationCount();

        Assertions.assertThat(cacheTestService.queryNullWithDefaultTtl(key)).isNull();

        String cacheKey = cacheKey("null-default-ttl", key);
        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
        Assertions.assertThat(cachedValue).isInstanceOf(NullCacheValue.class);
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.MILLISECONDS);
        Assertions.assertThat(ttl).isBetween(1L, 60_000L);

        Assertions.assertThat(cacheTestService.queryNullWithDefaultTtl(key)).isNull();
        Assertions.assertThat(cacheTestService.getNullQueryInvocationCount()).isEqualTo(invocationCount + 1);
    }

    @Test
    void testNullResultUsesCustomTtlAndIsCached() throws InterruptedException {
        String key = RandomStringUtils.random(32, true, true);
        int invocationCount = cacheTestService.getNullQueryInvocationCount();

        Assertions.assertThat(cacheTestService.queryNullWithCustomTtl(key)).isNull();

        String cacheKey = cacheKey("null-custom-ttl", key);
        Assertions.assertThat(redisTemplate.opsForValue().get(cacheKey)).isInstanceOf(NullCacheValue.class);
        Long ttl = redisTemplate.getExpire(cacheKey, TimeUnit.MILLISECONDS);
        Assertions.assertThat(ttl).isBetween(1L, 2_000L);

        // 空值占位命中时不应再次回源
        Assertions.assertThat(cacheTestService.queryNullWithCustomTtl(key)).isNull();
        Assertions.assertThat(cacheTestService.getNullQueryInvocationCount()).isEqualTo(invocationCount + 1);

        // TTL 到期后应删除占位值，并重新执行数据源方法
        waitForCacheExpiration(cacheKey);
        Assertions.assertThat(redisTemplate.hasKey(cacheKey)).isFalse();

        Assertions.assertThat(cacheTestService.queryNullWithCustomTtl(key)).isNull();
        Assertions.assertThat(cacheTestService.getNullQueryInvocationCount()).isEqualTo(invocationCount + 2);
        Assertions.assertThat(redisTemplate.opsForValue().get(cacheKey)).isInstanceOf(NullCacheValue.class);
    }

    private void waitForCacheExpiration(String cacheKey) throws InterruptedException {
        long deadline = System.currentTimeMillis() + 5_000L;
        while (redisTemplate.hasKey(cacheKey) && System.currentTimeMillis() < deadline) {
            TimeUnit.MILLISECONDS.sleep(100L);
        }
    }

    private String cacheKey(String name, String key) {
        return RedisKeyPrefix.CACHE.getKey() + name + ":" + key;
    }

    @Test
    void testEvictCache() {
        String orderNo = RandomStringUtils.random(32, true, true);
        String key = RedisKeyPrefix.CACHE.getKey() + "order:" + orderNo;
        redisTemplate.opsForValue().set(key, "xxx", 3600, TimeUnit.SECONDS);

        CreateOrderBO createOrderBO = new CreateOrderBO();
        createOrderBO.setOrderNo(orderNo);
        createOrderBO.setPrice(100L);
        cacheTestService.createOrder(createOrderBO);

        Assertions.assertThat(redisTemplate.opsForValue().get(orderNo)).isNull();
    }

}