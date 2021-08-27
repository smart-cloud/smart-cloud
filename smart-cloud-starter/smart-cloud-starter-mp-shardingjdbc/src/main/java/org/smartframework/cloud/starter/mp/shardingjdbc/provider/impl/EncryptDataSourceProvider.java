package org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.EncryptDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.AbstractDynamicShardingJdbcDataSourceProvider;

public class EncryptDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public EncryptDataSourceProvider(EncryptDataSource encryptDataSource) {
        setDataSourceAdapter(encryptDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.ENCRYPT_DATASOURCE);
    }

}