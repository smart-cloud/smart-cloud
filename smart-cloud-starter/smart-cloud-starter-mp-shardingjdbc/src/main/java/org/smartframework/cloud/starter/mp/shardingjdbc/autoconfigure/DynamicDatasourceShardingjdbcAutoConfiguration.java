package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
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
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.EncryptDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.MasterSlaveDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.ShadowDataSourceProvider;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl.ShardingDataSourceProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * 动态数据源与sharding jdbc集成配置
 *
 * @author collin
 * @date 2021-08-31
 */
@Configuration
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
@AutoConfigureAfter(SpringBootConfiguration.class)
public class DynamicDatasourceShardingjdbcAutoConfiguration {

    /**
     * 分片数据源
     *
     * @param shardingDataSource
     * @return
     */
    @Bean
    @Conditional(ShardingRuleCondition.class)
    @DependsOn(ShardingjdbcDatasourceNames.SHARDING_DATASOURCE)
    public DynamicDataSourceProvider dynamicShardingDataSourceProvider(final ShardingDataSource shardingDataSource) {
        return new ShardingDataSourceProvider(shardingDataSource);
    }

    /**
     * 主从数据源
     *
     * @param masterSlaveDataSource
     * @return
     */
    @Bean
    @Conditional(MasterSlaveRuleCondition.class)
    @DependsOn(ShardingjdbcDatasourceNames.MASTER_SLAVE_DATASOURCE)
    public DynamicDataSourceProvider dynamicMasterSlaveDataSourceProvider(final MasterSlaveDataSource masterSlaveDataSource) {
        return new MasterSlaveDataSourceProvider(masterSlaveDataSource);
    }

    /**
     * 脱敏数据源
     *
     * @param encryptDataSource
     * @return
     */
    @Bean
    @Conditional(EncryptRuleCondition.class)
    @DependsOn(ShardingjdbcDatasourceNames.ENCRYPT_DATASOURCE)
    public DynamicDataSourceProvider dynamicEncryptDataSourceProvider(final EncryptDataSource encryptDataSource) {
        return new EncryptDataSourceProvider(encryptDataSource);
    }

    /**
     * 影子数据源
     *
     * @param shadowDataSource
     * @return
     */
    @Bean
    @Conditional(ShadowRuleCondition.class)
    @DependsOn(ShardingjdbcDatasourceNames.SHADOW_DATASOURCE)
    public DynamicDataSourceProvider dynamicShadowDataSourceProvider(final ShadowDataSource shadowDataSource) {
        return new ShadowDataSourceProvider(shadowDataSource);
    }

}