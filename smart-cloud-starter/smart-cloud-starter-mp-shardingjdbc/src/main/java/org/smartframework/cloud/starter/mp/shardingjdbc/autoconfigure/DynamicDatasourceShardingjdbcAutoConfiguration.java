package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.EncryptDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShadowDataSource;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.ShardingRuleCondition;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.EncryptDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.MasterSlaveDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.ShadowDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.ShardingDataSourceProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
//@AutoConfigureAfter(SpringBootConfiguration.class)
public class DynamicDatasourceShardingjdbcAutoConfiguration {

    @Bean
    @Conditional(ShardingRuleCondition.class)
    public DynamicDataSourceProvider dynamicShardingDataSourceProvider(@Lazy ShardingDataSource shardingDataSource) {
        return new ShardingDataSourceProvider(shardingDataSource);
    }

    @Bean
    @ConditionalOnBean(MasterSlaveDataSource.class)
    public DynamicDataSourceProvider dynamicMasterSlaveDataSourceProvider(final MasterSlaveDataSource masterSlaveDataSource) {
        return new MasterSlaveDataSourceProvider(masterSlaveDataSource);
    }

    @Bean
    @ConditionalOnBean(EncryptDataSource.class)
    public DynamicDataSourceProvider dynamicEncryptDataSourceProvider(final EncryptDataSource encryptDataSource) {
        return new EncryptDataSourceProvider(encryptDataSource);
    }

    @Bean
    @ConditionalOnBean(ShadowDataSource.class)
    public DynamicDataSourceProvider dynamicShadowDataSourceProvider(final ShadowDataSource shadowDataSource) {
        return new ShadowDataSourceProvider(shadowDataSource);
    }

}