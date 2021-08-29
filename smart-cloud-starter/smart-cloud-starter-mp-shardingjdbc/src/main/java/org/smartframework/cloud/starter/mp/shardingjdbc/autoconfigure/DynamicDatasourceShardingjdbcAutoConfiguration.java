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

import javax.annotation.Resource;

@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
//@AutoConfigureAfter(SpringBootConfiguration.class)
public class DynamicDatasourceShardingjdbcAutoConfiguration {
    /**
     * 未使用分片, 脱敏的名称(默认): shardingDataSource
     * 使用了主从: masterSlaveDataSource
     * 根据自己场景修改注入
     */
    @Resource(name = "shardingDataSource")
    @Lazy
    private ShardingDataSource shardingDataSource;

    @Bean
    @Conditional(ShardingRuleCondition.class)
    public DynamicDataSourceProvider dynamicShardingDataSourceProvider() {
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