package org.smartframework.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mybatis.plus.test.prepare.mybatisplus.MybatisplusApp;
import org.smartframework.cloud.starter.mybatis.plus.util.DbTableUtil;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisplusApp.class, args = "--spring.profiles.active=mybatisplus")
class DbTableUtilTest {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Test
    void testCopyTableSchema() {
        boolean result = DbTableUtil.copyTableSchema("t_product_info", String.format("t_product_info_%s", RandomUtil.uuid()), dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void testCreateTableIfAbsent() {
        String targetTableName = String.format("t_product_info_%s", RandomUtil.uuid());
        // 不存在
        boolean result1 = DbTableUtil.createTableIfAbsent("t_product_info", targetTableName, dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result1).isTrue();

        // 存在
        boolean result2 = DbTableUtil.createTableIfAbsent("t_product_info", targetTableName, dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result2).isTrue();
    }

    @Test
    void testQueryTablesByPrefix() {
        List<String> productInfoTables = DbTableUtil.queryTablesByPrefix("t_product_info", dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(productInfoTables).isNotEmpty();
    }

}