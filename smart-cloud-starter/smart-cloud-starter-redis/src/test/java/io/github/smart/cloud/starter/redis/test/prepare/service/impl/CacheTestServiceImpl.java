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
package io.github.smart.cloud.starter.redis.test.prepare.service.impl;

import io.github.smart.cloud.starter.redis.annotation.CacheEvict;
import io.github.smart.cloud.starter.redis.annotation.Cacheable;
import io.github.smart.cloud.starter.redis.test.prepare.bo.CreateOrderBO;
import io.github.smart.cloud.starter.redis.test.prepare.dataobject.OrderInfo;
import io.github.smart.cloud.starter.redis.test.prepare.service.ICacheTestService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CacheTestServiceImpl implements ICacheTestService {

    private final AtomicInteger nullQueryInvocationCount = new AtomicInteger();

    @CacheEvict(name = "order:", expressions = {"#createOrderBO.orderNo"})
    @Override
    public OrderInfo createOrder(CreateOrderBO createOrderBO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(createOrderBO.getOrderNo());
        orderInfo.setPrice(createOrderBO.getPrice());

        return orderInfo;
    }

    @Cacheable(name = "order", expressions = {"#orderNo"}, cacheTtl = 3600, cacheUnit = TimeUnit.SECONDS)
    @Override
    public OrderInfo query(String orderNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setPrice(100L);
        return orderInfo;
    }

    @Cacheable(name = "null-without-cache", expressions = {"#key"}, cacheTtl = 3600, cacheUnit = TimeUnit.SECONDS)
    @Override
    public String queryNullWithoutCache(String key) {
        nullQueryInvocationCount.incrementAndGet();
        return null;
    }

    @Cacheable(name = "null-default-ttl", expressions = {"#key"}, cacheTtl = 3600,
            cacheUnit = TimeUnit.SECONDS, cacheNull = true)
    @Override
    public String queryNullWithDefaultTtl(String key) {
        nullQueryInvocationCount.incrementAndGet();
        return null;
    }

    @Cacheable(name = "null-custom-ttl", expressions = {"#key"}, cacheTtl = 3600,
            cacheUnit = TimeUnit.SECONDS, cacheNull = true, cacheNullTtl = 2, cacheNullUnit = TimeUnit.SECONDS)
    @Override
    public String queryNullWithCustomTtl(String key) {
        nullQueryInvocationCount.incrementAndGet();
        return null;
    }

    @Override
    public int getNullQueryInvocationCount() {
        return nullQueryInvocationCount.get();
    }

}