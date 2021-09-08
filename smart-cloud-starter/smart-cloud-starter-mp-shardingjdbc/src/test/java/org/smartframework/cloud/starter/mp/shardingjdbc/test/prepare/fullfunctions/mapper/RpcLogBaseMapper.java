package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.RpcLogEntity;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.SmartMapper;

/**
 * rpc日志信息base mapper
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Mapper
public interface RpcLogBaseMapper extends SmartMapper<RpcLogEntity> {

}