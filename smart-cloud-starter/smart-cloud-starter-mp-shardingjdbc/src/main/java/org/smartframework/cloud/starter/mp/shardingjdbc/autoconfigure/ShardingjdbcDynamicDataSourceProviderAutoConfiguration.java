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
package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.smartframework.cloud.starter.mp.shardingjdbc.condition.DynamicRoutingDataSourceCondition;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingSphereDataSourceName;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源Provider与sharding jdbc数据源集成配置
 *
 * @author collin
 * @date 2021-08-31
 */
@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
@AutoConfigureAfter(ShardingSphereAutoConfiguration.class)
@Conditional(DynamicRoutingDataSourceCondition.class)
public class ShardingjdbcDynamicDataSourceProviderAutoConfiguration {

    /**
     * sharding jdbc数据源
     *
     * @param shardingDataSource
     * @return
     */
    @Bean
    @DependsOn(ShardingSphereDataSourceName.SHARDING_DATASOURCE)
    public DynamicDataSourceProvider dynamicShardingSphereDataSourceProvider(final ShardingSphereDataSource shardingDataSource) {
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                // 多数据源集合
                Map<String, DataSource> dynamicDataSourceMap = new HashMap<>(1);
                dynamicDataSourceMap.put(ShardingSphereDataSourceName.SHARDING_DATASOURCE, shardingDataSource);
                return dynamicDataSourceMap;
            }
        };
    }

}