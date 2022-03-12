package io.github.smart.cloud.starter.redis.test.prepare.service;

import io.github.smart.cloud.starter.redis.test.prepare.bo.CreateOrderBO;
import io.github.smart.cloud.starter.redis.test.prepare.dataobject.OrderInfo;

public interface ICacheTestService {

    /**
     * 创建订单
     *
     * @param createOrderBO
     * @return
     */
    OrderInfo createOrder(CreateOrderBO createOrderBO);

    /**
     * 查询订单
     *
     * @param orderNo
     * @return
     */
    OrderInfo query(String orderNo);

}