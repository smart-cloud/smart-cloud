package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.biz;

import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.mapper.OrderBillBaseMapper;
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