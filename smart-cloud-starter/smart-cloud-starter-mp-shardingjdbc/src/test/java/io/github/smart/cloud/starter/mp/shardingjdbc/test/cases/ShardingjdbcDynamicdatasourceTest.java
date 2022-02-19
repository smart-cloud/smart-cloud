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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.ShardingJdbcDynamicDatasourceApp;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.ApiLogBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.OrderBillBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.ProductInfoBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.RpcLogBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ApiLogEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.OrderBillEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ProductInfoEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.RpcLogEntity;
import io.github.smart.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 分片数据源+动态数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcDynamicDatasourceApp.class, args = "--spring.profiles.active=shardingjdbcdynamicdatasource")
class ShardingjdbcDynamicdatasourceTest {

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
        ApiLogEntity apiLogEntity = apiLogBiz.buildEntity();
        apiLogEntity.setApiDesc("test");
        boolean saveResult = apiLogBiz.save(apiLogEntity);
        Assertions.assertThat(saveResult).isTrue();

        ApiLogEntity entity = apiLogBiz.getById(apiLogEntity.getId());
        Assertions.assertThat(entity).isNotNull();
    }

    /**
     * 分表
     */
    @Test
    void testShardingJdbcOrder() {
        OrderBillEntity orderBillEntity = orderBillBiz.buildEntity();
        orderBillEntity.setOrderNo(RandomUtil.generateRandom(false, 32));
        orderBillEntity.setAmount(100L);
        orderBillEntity.setStatus((byte) 1);
        orderBillEntity.setPayState((byte) 1);
        orderBillEntity.setBuyer(1L);
        orderBillEntity.setInsertUser(1L);
        boolean saveResult = orderBillBiz.save(orderBillEntity);
        Assertions.assertThat(saveResult).isTrue();

        OrderBillEntity entity = orderBillBiz.getById(orderBillEntity.getId());
        Assertions.assertThat(entity).isNotNull();
    }

    /**
     * 分表库中非分表表
     */
    @Test
    void testShardingJdbcRpcLog() {
        RpcLogEntity rpcLogEntity = rpcLogBiz.buildEntity();
        rpcLogEntity.setApiDesc("test");
        boolean saveResult = rpcLogBiz.save(rpcLogEntity);
        Assertions.assertThat(saveResult).isTrue();

        RpcLogEntity entity = rpcLogBiz.getById(rpcLogEntity.getId());
        Assertions.assertThat(entity).isNotNull();
    }

    /**
     * 分表
     */
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
