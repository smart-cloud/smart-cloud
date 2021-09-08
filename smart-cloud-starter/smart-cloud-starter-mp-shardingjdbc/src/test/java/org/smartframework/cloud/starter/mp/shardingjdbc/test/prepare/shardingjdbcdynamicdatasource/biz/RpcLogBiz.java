package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.constants.DatasourceName;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.RpcLogEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.mapper.RpcLogBaseMapper;
import org.smartframework.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import org.springframework.stereotype.Repository;

/**
 * 日志rpc biz
 *
 * @author liyulin
 * @date 2019-04-08
 */
@Repository
@DS(DatasourceName.ORDER)
public class RpcLogBiz extends BaseBiz<RpcLogBaseMapper, RpcLogEntity> {

}