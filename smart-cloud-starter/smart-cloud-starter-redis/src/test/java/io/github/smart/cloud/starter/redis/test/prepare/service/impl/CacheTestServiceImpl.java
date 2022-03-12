package io.github.smart.cloud.starter.redis.test.prepare.service.impl;

import io.github.smart.cloud.starter.redis.annotation.CacheEvict;
import io.github.smart.cloud.starter.redis.annotation.Cacheable;
import io.github.smart.cloud.starter.redis.test.prepare.bo.CreateOrderBO;
import io.github.smart.cloud.starter.redis.test.prepare.dataobject.OrderInfo;
import io.github.smart.cloud.starter.redis.test.prepare.service.ICacheTestService;
import org.springframework.stereotype.Service;

@Service
public class CacheTestServiceImpl implements ICacheTestService {

    @CacheEvict(name = "order:", expressions = {"#createOrderBO.orderNo"})
    @Override
    public OrderInfo createOrder(CreateOrderBO createOrderBO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(createOrderBO.getOrderNo());
        orderInfo.setPrice(createOrderBO.getPrice());

        return orderInfo;
    }

    @Cacheable(name = "order", expressions = {"#orderNo"}, ttl = 3600)
    @Override
    public OrderInfo query(String orderNo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setPrice(100L);
        return orderInfo;
    }

}