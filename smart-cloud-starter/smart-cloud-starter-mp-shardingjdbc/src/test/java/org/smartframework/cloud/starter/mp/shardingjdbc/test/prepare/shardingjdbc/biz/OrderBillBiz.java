package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz;

import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.mapper.OrderBillBaseMapper;
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

}