package org.smartframework.cloud.starter.mp.shardingjdbc.provider;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Setter(AccessLevel.PROTECTED)
public abstract class AbstractDynamicShardingJdbcDataSourceProvider extends AbstractDataSourceProvider {

    private AbstractDataSourceAdapter dataSourceAdapter;
    private String datasourceName;

    @Override
    public Map<String, DataSource> loadDataSources() {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(datasourceName, dataSourceAdapter);
        return dataSourceMap;
    }

}