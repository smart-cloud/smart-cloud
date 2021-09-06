package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.ShardingJdbcDynamicDatasourceApp;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.ApiLogBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.OrderBillBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.biz.ProductInfoBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ApiLogEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource.entity.ProductInfoEntity;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 分片数据源+动态数据源集成测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcDynamicDatasourceApp.class, args = "--spring.profiles.active=shardingjdbcdynamicdatasource")
class ShardingjdbcdynamicdatasourceTest {

    @Autowired
    private ApiLogBiz apiLogBiz;
    @Autowired
    private OrderBillBiz orderBillBiz;
    @Autowired
    private ProductInfoBiz productInfoBiz;

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
