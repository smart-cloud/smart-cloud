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
package io.github.smart.cloud.starter.mybatis.plus.test.cases;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.RedisMockServerAutoConfiguration;
import io.github.smart.cloud.starter.mybatis.plus.test.prepare.mybatisplus.MybatisplusApp;
import io.github.smart.cloud.starter.mybatis.plus.util.DbTableUtil;
import io.github.smart.cloud.utility.RandomUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@Import(RedisMockServerAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisplusApp.class, args = "--spring.profiles.active=mybatisplus")
class DbTableUtilTest {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    /**
     * 因h2不支持“create table xxxyyy like xxx”，所以跳过
     */
    @Disabled
    @Test
    void testCopyTableSchema() {
        boolean result = DbTableUtil.copyTableSchema("t_product_info", String.format("t_product_info_%s", RandomUtil.uuid()), dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result).isTrue();
    }

    /**
     * 因h2不支持“create table xxxyyy like xxx”，所以跳过
     */
    @Disabled
    @Test
    void testCreateTableIfAbsent() {
        String sourceTableName = "t_product_info";
        String targetTableName = String.format("_%s", sourceTableName, RandomUtil.uuid());
        // 不存在
        boolean result1 = DbTableUtil.createTableIfAbsent(sourceTableName, targetTableName, dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result1).isTrue();

        // 存在
        boolean result2 = DbTableUtil.createTableIfAbsent(sourceTableName, targetTableName, dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(result2).isTrue();
    }

    @Test
    void testExistTable() {
        Assertions.assertThat(DbTableUtil.existTable("xxx", dynamicRoutingDataSource.determineDataSource())).isFalse();
        Assertions.assertThat(DbTableUtil.existTable("t_product_info", dynamicRoutingDataSource.determineDataSource())).isTrue();
    }

    @Test
    void testQueryTablesByPrefix() {
        List<String> productInfoTables = DbTableUtil.queryTablesByPrefix("t_product_info", dynamicRoutingDataSource.determineDataSource());
        Assertions.assertThat(productInfoTables).isNotEmpty();
    }

}