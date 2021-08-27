package org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.AbstractDynamicShardingJdbcDataSourceProvider;

public class ShardingDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public ShardingDataSourceProvider(ShardingDataSource shardingDataSource) {
        setDataSourceAdapter(shardingDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.SHARDING_DATASOURCE);
    }

}