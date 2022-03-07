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
package io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.constants.DatasourceName;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.RpcLogEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.mapper.RpcLogBaseMapper;
import io.github.smart.cloud.starter.mybatis.plus.common.biz.BaseBiz;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.utility.NonceUtil;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 日志rpc biz
 *
 * @author collin
 * @date 2019-04-08
 */
@Repository
@DS(DatasourceName.SHARDING_DATASOURCE)
public class RpcLogBiz extends BaseBiz<RpcLogBaseMapper, RpcLogEntity> {

    public RpcLogEntity insert(String apiDesc) {
        RpcLogEntity rpcLogEntity = new RpcLogEntity();
        rpcLogEntity.setId(NonceUtil.nextId());
        rpcLogEntity.setInsertTime(new Date());
        rpcLogEntity.setDelState(DeleteState.NORMAL);
        rpcLogEntity.setApiDesc(apiDesc);
        save(rpcLogEntity);

        return rpcLogEntity;
    }

    public RpcLogEntity getFromMaster(Long id) {
        try (HintManager hintManager = HintManager.getInstance();) {
            hintManager.setWriteRouteOnly();
            return super.getById(id);
        }
    }

    public RpcLogEntity getFromSlave(Long id) {
        return super.getById(id);
    }

}