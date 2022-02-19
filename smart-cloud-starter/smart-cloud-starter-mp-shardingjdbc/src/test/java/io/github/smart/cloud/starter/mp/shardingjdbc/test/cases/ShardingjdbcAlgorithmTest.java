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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.ShardingJdbcAlgorithmApp;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.biz.OrderBillBiz;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.entity.OrderBillEntity;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.util.OrderUtil;
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