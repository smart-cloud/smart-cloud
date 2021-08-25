package org.smartframework.cloud.starter.mp.shardingjdbc.provider;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;

public class ShardingDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public ShardingDataSourceProvider(ShardingDataSource shardingDataSource) {
        setDataSourceAdapter(shardingDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.SHARDING_DATASOURCE);
    }

}