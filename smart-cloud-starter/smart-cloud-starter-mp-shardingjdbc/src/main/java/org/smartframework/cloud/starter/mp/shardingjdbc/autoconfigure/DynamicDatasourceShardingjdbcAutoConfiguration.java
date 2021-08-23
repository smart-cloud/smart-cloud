package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.apache.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.EncryptDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShadowDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.apache.shardingsphere.shardingjdbc.spring.boot.encrypt.EncryptRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.MasterSlaveRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.shadow.ShadowRuleCondition;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.ShardingRuleCondition;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
@AutoConfigureAfter(SpringBootConfiguration.class)
public class DynamicDatasourceShardingjdbcAutoConfiguration {

    @Bean
    @Conditional({ShardingRuleCondition.class})
    public DynamicDataSourceProvider dynamicShardingDataSourceProvider(final ShardingDataSource shardingDataSource) {
        return createDynamicShardingJdbcDataSourceProvider(shardingDataSource, ShardingjdbcDatasourceNames.SHARDING_DATASOURCE);
    }

    @Bean
    @Conditional({MasterSlaveRuleCondition.class})
    public DynamicDataSourceProvider dynamicMasterSlaveDataSourceProvider(final MasterSlaveDataSource masterSlaveDataSource) {
        return createDynamicShardingJdbcDataSourceProvider(masterSlaveDataSource, ShardingjdbcDatasourceNames.MASTER_SLAVE_DATASOURCE);
    }

    @Bean
    @Conditional({EncryptRuleCondition.class})
    public DynamicDataSourceProvider dynamicEncryptDataSourceProvider(final EncryptDataSource encryptDataSource) {
        return createDynamicShardingJdbcDataSourceProvider(encryptDataSource, ShardingjdbcDatasourceNames.ENCRYPT_DATASOURCE);
    }

    @Bean
    @Conditional({ShadowRuleCondition.class})
    public DynamicDataSourceProvider dynamicShadowDataSourceProvider(final ShadowDataSource shadowDataSource) {
        return createDynamicShardingJdbcDataSourceProvider(shadowDataSource, ShardingjdbcDatasourceNames.SHADOW_DATASOURCE);
    }

    private DynamicDataSourceProvider createDynamicShardingJdbcDataSourceProvider(@NotNull final AbstractDataSourceAdapter dataSourceAdapter, @NotBlank final String datasourceName) {
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dataSourceMap = new HashMap<>();
                dataSourceMap.put(datasourceName, dataSourceAdapter);
                dataSourceMap.putAll(dataSourceAdapter.getDataSourceMap());
                return dataSourceMap;
            }
        };
    }

}