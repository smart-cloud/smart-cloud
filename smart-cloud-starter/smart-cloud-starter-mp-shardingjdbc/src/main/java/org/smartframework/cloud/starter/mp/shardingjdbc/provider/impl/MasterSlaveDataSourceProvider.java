package org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.MasterSlaveDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.AbstractDynamicShardingJdbcDataSourceProvider;

public class MasterSlaveDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public MasterSlaveDataSourceProvider(MasterSlaveDataSource masterSlaveDataSource) {
        setDataSourceAdapter(masterSlaveDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.MASTER_SLAVE_DATASOURCE);
    }

}