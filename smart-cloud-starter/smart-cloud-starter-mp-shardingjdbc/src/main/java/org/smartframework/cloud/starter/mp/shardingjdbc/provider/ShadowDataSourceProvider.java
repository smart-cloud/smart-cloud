package org.smartframework.cloud.starter.mp.shardingjdbc.provider;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShadowDataSource;
import org.smartframework.cloud.starter.mp.shardingjdbc.constants.ShardingjdbcDatasourceNames;

public class ShadowDataSourceProvider extends AbstractDynamicShardingJdbcDataSourceProvider {

    public ShadowDataSourceProvider(ShadowDataSource shadowDataSource) {
        setDataSourceAdapter(shadowDataSource);
        setDatasourceName(ShardingjdbcDatasourceNames.SHADOW_DATASOURCE);
    }

}