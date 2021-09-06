package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.SmartMapper;

/**
 * 订单信息base mapper
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Mapper
public interface OrderBillBaseMapper extends SmartMapper<OrderBillEntity> {

}