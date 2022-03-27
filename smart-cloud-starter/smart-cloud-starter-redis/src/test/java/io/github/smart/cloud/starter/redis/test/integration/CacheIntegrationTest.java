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

import io.github.smart.cloud.starter.redis.test.prepare.bo.CreateOrderBO;
import io.github.smart.cloud.starter.redis.test.prepare.dataobject.OrderInfo;
import io.github.smart.cloud.starter.redis.test.prepare.service.ICacheTestService;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

class CacheIntegrationTest extends AbstractRedisIntegrationTest {

    @Autowired
    private ICacheTestService cacheTestService;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    void testCacheable() {
        String orderNo = RandomStringUtils.random(32, true, true);
        OrderInfo orderInfoCache = cacheTestService.query(orderNo);
        RMapCache<String, OrderInfo> mapCache = redissonClient.getMapCache(getCacheName());

        Assertions.assertThat(mapCache.get(orderNo)).isNotNull();
        Assertions.assertThat(mapCache.get(orderNo).getOrderNo()).isNotBlank();
        Assertions.assertThat(mapCache.get(orderNo).getOrderNo()).isEqualTo(orderInfoCache.getOrderNo());
        Assertions.assertThat(mapCache.get(orderNo).getPrice()).isNotNull();
        Assertions.assertThat(mapCache.get(orderNo).getPrice()).isEqualTo(orderInfoCache.getPrice());
    }

    private String getCacheName() {
        return "cache:order";
    }

    @Test
    void testEvictCache() {
        String orderNo = RandomStringUtils.random(32, true, true);
        RMapCache<String, OrderInfo> mapCache = redissonClient.getMapCache(getCacheName());
        mapCache.put(orderNo, new OrderInfo(), 3600, TimeUnit.SECONDS);
        Assertions.assertThat(mapCache.get(orderNo)).isNotNull();

        CreateOrderBO createOrderBO = new CreateOrderBO();
        createOrderBO.setOrderNo(orderNo);
        createOrderBO.setPrice(100L);
        cacheTestService.createOrder(createOrderBO);

        Assertions.assertThat(mapCache.get(orderNo)).isNull();
    }

}