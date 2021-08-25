package org.smartframework.cloud.starter.mp.shardingjdbc.provider;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.EncryptDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;

public class EncryptDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public EncryptDataSourceProvider(EncryptDataSource encryptDataSource) {
        setDataSourceAdapter(encryptDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.ENCRYPT_DATASOURCE);
    }

}