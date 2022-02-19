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

import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.ShardingJdbcShardingMasterSlaveApp;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.biz.OrderBillBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.biz.ProductInfoBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.entity.OrderBillEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.entity.ProductInfoEntity;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 分片+主从数据源+dynamic主从数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcShardingMasterSlaveApp.class, args = "--spring.profiles.active=shardingjdbcshardingmasterslave")
class ShardingJdbcShardingMasterSlaveTest {

    @Autowired
    private OrderBillBiz orderBillBiz;
    @Autowired
    private ProductInfoBiz productInfoBiz;

    @Test
    void testShardingJdbcMasterSlaveOrder() {
        OrderBillEntity orderBillEntity = orderBillBiz.buildEntity();
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

    @Test
    void testShardingJdbcProduct() {
        ProductInfoEntity productInfoEntity = productInfoBiz.buildEntity();
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