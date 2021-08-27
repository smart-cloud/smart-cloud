package org.smartframework.cloud.starter.mp.shardingjdbc.provider.impl;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShadowDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;
import org.smartframework.cloud.starter.mp.shardingjdbc.provider.AbstractDynamicShardingJdbcDataSourceProvider;

public class ShadowDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public ShadowDataSourceProvider(ShadowDataSource shadowDataSource) {
        setDataSourceAdapter(shadowDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.SHADOW_DATASOURCE);
    }

}