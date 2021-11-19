package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.ShardingJdbcAlgorithmApp;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.biz.OrderBillBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.entity.OrderBillEntity;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 自定义分片算法测试
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcAlgorithmApp.class, args = "--spring.profiles.active=shardingjdbcalgorithm")
public class ShardingjdbcAlgorithmTest {

    @Autowired
    private OrderBillBiz orderBillBiz;

    @BeforeEach
    void cleanData() {
        orderBillBiz.truncate();
    }

    @Test
    void testShardingJdbcOrder() {
        Long uid = 100L;
        OrderBillEntity orderBillEntity = orderBillBiz.buildEntity();
        orderBillEntity.setOrderNo(OrderUtil.generateOrderNo(uid));
        orderBillEntity.setAmount(100L);
        orderBillEntity.setStatus((byte) 1);
        orderBillEntity.setPayState((byte) 1);
        orderBillEntity.setBuyer(uid);
        orderBillEntity.setInsertUser(1L);
        boolean saveResult = orderBillBiz.save(orderBillEntity);
        Assertions.assertThat(saveResult).isTrue();

        OrderBillEntity entity1 = orderBillBiz.getByOrderNo(orderBillEntity.getOrderNo());
        Assertions.assertThat(entity1).isNotNull();

        OrderBillEntity entity2 = orderBillBiz.getByBuyer(orderBillEntity.getBuyer());
        Assertions.assertThat(entity2).isNotNull();
    }

}