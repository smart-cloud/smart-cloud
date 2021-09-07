package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz.OrderBillBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz.ProductInfoBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.entity.ProductInfoEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcmasterslave.ShardingJdbcMasterSlaveApp;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 分片+主从数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcMasterSlaveApp.class, args = "--spring.profiles.active=shardingjdbcmasterslave")
public class ShardingJdbcMasterSlaveTest {

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

        OrderBillEntity entity = orderBillBiz.getById(orderBillEntity.getId());
        Assertions.assertThat(entity).isNotNull();
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