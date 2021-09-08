package org.smartframework.cloud.starter.mp.shardingjdbc.provider;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.core.rule.MasterSlaveRule;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.context.ShardingRuntimeContext;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

import javax.sql.DataSource;
import java.util.*;

@Setter(AccessLevel.PROTECTED)
public abstract class AbstractDynamicShardingJdbcDataSourceProvider extends AbstractDataSourceProvider {

    private AbstractDataSourceAdapter dataSourceAdapter;
    private String datasourceName;

    @Override
    public Map<String, DataSource> loadDataSources() {
        // 多数据源集合
        Map<String, DataSource> dynamicDataSourceMap = new HashMap<>();
        // 普通数据源
        dynamicDataSourceMap.put(datasourceName, dataSourceAdapter);

        // 处理sharding jdbc的从库数据源：将sharding slave的数据源名字中的“-”替换成“_”，以便dynamic-datasource分组处理
        Set<String> shardingSlaveDatasourceNames = collectShardingSlaveDatasourceNames(dataSourceAdapter);
        if (shardingSlaveDatasourceNames.size() == 0) {
            dynamicDataSourceMap.putAll(dataSourceAdapter.getDataSourceMap());
        } else {
            dataSourceAdapter.getDataSourceMap().forEach((shardingDatasourceName, datasource) -> {
                String finalShardingDatasourceName = shardingDatasourceName;
                if (shardingSlaveDatasourceNames.contains(shardingDatasourceName)) {
                    // 将sharding slave的数据源名字中的“-”替换成“_”，以便dynamic-datasource分组处理
                    // @see DynamicRoutingDataSource#addGroupDataSource
                    finalShardingDatasourceName = shardingDatasourceName.replaceAll("-", "_");
                }
                dynamicDataSourceMap.put(finalShardingDatasourceName, datasource);
            });
        }
        return dynamicDataSourceMap;
    }

    /**
     * 获取shardingjdbc的从库数据源名称
     *
     * @param dataSourceAdapter
     * @return
     */
    private Set<String> collectShardingSlaveDatasourceNames(AbstractDataSourceAdapter dataSourceAdapter) {
        if (!(dataSourceAdapter instanceof ShardingDataSource)) {
            return new HashSet<>(0);
        }
        ShardingDataSource shardingDataSource = (ShardingDataSource) dataSourceAdapter;
        ShardingRuntimeContext runtimeContext = shardingDataSource.getRuntimeContext();
        if (runtimeContext == null) {
            return new HashSet<>(0);
        }
        ShardingRule shardingRule = runtimeContext.getRule();
        if (shardingRule == null) {
            return new HashSet<>(0);
        }
        Collection<MasterSlaveRule> masterSlaveRules = shardingRule.getMasterSlaveRules();
        if (CollectionUtils.isEmpty(masterSlaveRules)) {
            return new HashSet<>(0);
        }

        Set<String> slaveDataSourceNames = new HashSet<>(4);
        for (MasterSlaveRule masterSlaveRule : masterSlaveRules) {
            slaveDataSourceNames.addAll(masterSlaveRule.getSlaveDataSourceNames());
        }
        return slaveDataSourceNames;
    }

}