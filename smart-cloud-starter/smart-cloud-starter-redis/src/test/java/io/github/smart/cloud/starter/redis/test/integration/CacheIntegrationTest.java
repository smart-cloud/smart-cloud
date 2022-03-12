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