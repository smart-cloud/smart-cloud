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
package io.github.smart.cloud.starter.mp.shardingjdbc.test.cases;

import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.ShardingJdbcFullFunctionsApp;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.ApiLogBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.OrderBillBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.ProductInfoBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.RpcLogBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.ApiLogEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.OrderBillEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.RpcLogEntity;
import io.github.smart.cloud.starter.mybatis.plus.enums.DeleteState;
import io.github.smart.cloud.utility.NonceUtil;
import io.github.smart.cloud.utility.RandomUtil;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

/**
 * 分片+主从数据源+多数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcFullFunctionsApp.class, args = "--spring.profiles.active=fullfunctions")
class FullFunctionsTest extends AbstractIntegrationTest {

    @Autowired
    private ApiLogBiz apiLogBiz;
    @Autowired
    private OrderBillBiz orderBillBiz;
    @Autowired
    private RpcLogBiz rpcLogBiz;
    @Autowired
    private ProductInfoBiz productInfoBiz;

    /**
     * 普通数据源
     */
    @Test
    void testDynamicDatasource() {
        ApiLogEntity apiLogEntity = new ApiLogEntity();
        apiLogEntity.setId(NonceUtil.nextId());
        apiLogEntity.setApiDesc("test");
        apiLogEntity.setInsertTime(new Date());
        apiLogEntity.setDelState(DeleteState.NORMAL);
        boolean saveResult = apiLogBiz.save(apiLogEntity);
        Assertions.assertThat(saveResult).isTrue();

        ApiLogEntity entity = apiLogBiz.getById(apiLogEntity.getId());
        Assertions.assertThat(entity).isNotNull();
    }

    @Test
    void testShardingJdbcMasterSlaveOrder() {
        OrderBillEntity orderBillEntity = new OrderBillEntity();
        orderBillEntity.setId(NonceUtil.nextId());
        orderBillEntity.setInsertTime(new Date());
        orderBillEntity.setDelState(DeleteState.NORMAL);
        orderBillEntity.setOrderNo(RandomUtil.generateRandom(false, 32));
        orderBillEntity.setAmount(100L);
        orderBillEntity.setStatus((byte) 1);
        orderBillEntity.setPayState((byte) 1);
        orderBillEntity.setBuyer(1L);
        orderBillEntity.setInsertUser(1L);
        boolean saveResult = orderBillBiz.save(orderBillEntity);
        Assertions.assertThat(saveResult).isTrue();

        // 默认查从库
        OrderBillEntity slaveEntity = orderBillBiz.getById(orderBillEntity.getId());
        Assertions.assertThat(slaveEntity).isNull();

        // 强制查主库
        try (HintManager hintManager = HintManager.getInstance();) {
            hintManager.setWriteRouteOnly();
            OrderBillEntity masterEntity = orderBillBiz.getById(orderBillEntity.getId());
            Assertions.assertThat(masterEntity).isNotNull();
        }
    }

    /**
     * 分表库中非分表表
     */
    @Test
    void testShardingJdbcRpcLog() {
        RpcLogEntity rpcLogEntity = rpcLogBiz.insert("test");

        // 默认查从库
        RpcLogEntity slaveEntity = rpcLogBiz.getFromSlave(rpcLogEntity.getId());
        Assertions.assertThat(slaveEntity).isNull();

        // 强制查主库
        RpcLogEntity masterEntity = rpcLogBiz.getFromMaster(rpcLogEntity.getId());
        Assertions.assertThat(masterEntity).isNotNull();
    }

    @Test
    void testShardingJdbcProduct() {
        ProductInfoEntity productInfoEntity = new ProductInfoEntity();
        productInfoEntity.setId(NonceUtil.nextId());
        productInfoEntity.setInsertTime(new Date());
        productInfoEntity.setDelState(DeleteState.NORMAL);
        productInfoEntity.setName(RandomUtil.generateRandom(false, 6));
        productInfoEntity.setSellPrice(100L);
        productInfoEntity.setStock(100L);
        productInfoEntity.setInsertUser(1L);
        boolean saveResult = productInfoBiz.save(productInfoEntity);
        Assertions.assertThat(saveResult).isTrue();

        ProductInfoEntity entity = productInfoBiz.getById(productInfoEntity.getId());
        Assertions.assertThat(entity).isNotNull();
    }

}