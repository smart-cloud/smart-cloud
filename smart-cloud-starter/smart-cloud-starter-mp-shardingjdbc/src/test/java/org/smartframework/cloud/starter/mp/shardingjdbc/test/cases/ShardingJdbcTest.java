package org.smartframework.cloud.starter.mp.shardingjdbc.test.cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.ShardingJdbcApp;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz.ApiLogBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz.OrderBillBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.biz.ProductInfoBiz;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbc.entity.ApiLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingJdbcApp.class, args = "--spring.profiles.active=shardingjdbc")
class ShardingJdbcTest {

    @Autowired
    private ApiLogBiz apiLogBiz;
    @Autowired
    private OrderBillBiz orderBillBiz;
    @Autowired
    private ProductInfoBiz productInfoBiz;

    @Test
    void testCreate() {
        ApiLogEntity apiLogEntity = apiLogBiz.buildEntity();
        apiLogEntity.setApiDesc("test");
        apiLogBiz.save(apiLogEntity);

        ApiLogEntity logs = apiLogBiz.getById(apiLogEntity.getId());
    }

}