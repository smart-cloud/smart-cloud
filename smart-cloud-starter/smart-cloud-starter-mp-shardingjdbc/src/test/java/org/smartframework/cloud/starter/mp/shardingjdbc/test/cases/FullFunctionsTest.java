package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.apache.shardingsphere.infra.hint.HintManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.ShardingJdbcFullFunctionsApp;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.ApiLogBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.OrderBillBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.ProductInfoBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.biz.RpcLogBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.ApiLogEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions.entity.RpcLogEntity;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 分片+主从数据源+多数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcFullFunctionsApp.class, args = "--spring.profiles.active=fullfunctions")
class FullFunctionsTest {

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