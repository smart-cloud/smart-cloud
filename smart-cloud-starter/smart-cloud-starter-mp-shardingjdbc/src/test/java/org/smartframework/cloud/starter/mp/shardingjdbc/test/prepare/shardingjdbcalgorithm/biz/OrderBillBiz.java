package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.mapper.OrderBillBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.springframework.stereotype.Repository;

/**
 * 订单信息api biz
 *
 * @author liyulin
 * @date 2019-04-08
 */
@Repository
public class OrderBillBiz extends BaseBiz<OrderBillBaseMapper, OrderBillEntity> {

    public OrderBillEntity getByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderBillEntity::getOrderNo, orderNo);
        return getOne(queryWrapper);
    }

    public OrderBillEntity getByBuyer(Long buyer) {
        LambdaQueryWrapper<OrderBillEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderBillEntity::getBuyer, buyer);
        return getOne(queryWrapper);
    }

}