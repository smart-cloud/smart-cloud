package org.smartframework.cloud.starter.mybatis.autoconfigure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.context.ShardingRuntimeContext;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.mybatis.constants.ShardingJdbcConstants;
import org.smartframework.cloud.starter.mybatis.constants.ShardingJdbcDS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 多数据源集成sharding jdbc
 *
 * @author collin
 * @date 2021-02-09
 */
@Configuration
@ConditionalOnClass(ShardingDataSource.class)
@AutoConfigureBefore(DynamicDataSourceAutoConfiguration.class)
@Slf4j
public class SmartDataSourceAutoConfiguration {

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Primary
    @Bean
    @DependsOn("shardingDataSource")
    public DynamicDataSourceProvider smartDynamicDataSourceProvider(final ShardingDataSource shardingDataSource) {
        Map<String, DataSourceProperty> dynamicDataSourcePropertiesMap = dynamicDataSourceProperties.getDatasource();
        return new AbstractDataSourceProvider() {
            @Override
            public Map<String, DataSource> loadDataSources() {
                Map<String, DataSource> dynamicDataSourceMap = createDataSourceMap(dynamicDataSourcePropertiesMap);
                try {
                    putShardingDatasource(shardingDataSource, false, dynamicDataSourceMap);
                    putShardingDatasource(shardingDataSource, true, dynamicDataSourceMap);
                } catch (SQLException e) {
                    log.error("create ShardingDataSource fail", e);
                }
                return dynamicDataSourceMap;
            }
        };
    }

    private void putShardingDatasource(ShardingDataSource shardingDataSource, boolean slave, Map<String, DataSource> dynamicDataSourceMap) throws SQLException {
        Map<String, DataSource> shardingDataSourceMap = shardingDataSource.getDataSourceMap();
        Map<String, DataSource> shardingDataSourceMapTemp = shardingDataSourceMap.entrySet()
                .stream()
                .filter(entry -> slave ? isSlave(entry.getKey()) : isMaster(entry.getKey()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        if (MapUtils.isNotEmpty(shardingDataSourceMapTemp)) {
            String dsName = slave ? ShardingJdbcDS.SLAVE : ShardingJdbcDS.MASTER;
            if (shardingDataSourceMapTemp.size() == shardingDataSourceMap.size()) {
                dynamicDataSourceMap.put(dsName, shardingDataSource);
            } else {
                ShardingRuntimeContext shardingRuntimeContext = shardingDataSource.getRuntimeContext();
                ShardingRule shardingDataSourceRule = shardingRuntimeContext.getRule();

                ShardingRuleConfiguration shardingRuleConfigurationTemp = new ShardingRuleConfiguration();
                BeanUtils.copyProperties(shardingDataSourceRule.getRuleConfiguration(), shardingRuleConfigurationTemp);

                Collection<TableRuleConfiguration> tableRuleConfigs = shardingRuleConfigurationTemp.getTableRuleConfigs();
                Collection<TableRuleConfiguration> finalTableRuleConfigs = new ArrayList<>();
                tableRuleConfigs.forEach(tableRuleConfig -> {
                    String actualDataNodes = tableRuleConfig.getActualDataNodes();
                    String[] actualDataNodeArray = actualDataNodes.split(SymbolConstant.COMMA);
                    String finalActualDataNodes = Stream.of(actualDataNodeArray).filter(item -> slave ? isSlave(item) : isMaster(item)).collect(Collectors.joining(SymbolConstant.COMMA));
                    if (StringUtils.isNotBlank(finalActualDataNodes)) {
                        TableRuleConfiguration finalTableRuleConfiguration = new TableRuleConfiguration(tableRuleConfig.getLogicTable(), finalActualDataNodes);
                        finalTableRuleConfiguration.setDatabaseShardingStrategyConfig(tableRuleConfig.getDatabaseShardingStrategyConfig());
                        finalTableRuleConfiguration.setTableShardingStrategyConfig(tableRuleConfig.getTableShardingStrategyConfig());
                        finalTableRuleConfiguration.setKeyGeneratorConfig(tableRuleConfig.getKeyGeneratorConfig());
                        finalTableRuleConfigs.add(finalTableRuleConfiguration);
                    }
                });
                shardingRuleConfigurationTemp.setTableRuleConfigs(finalTableRuleConfigs);

                Collection<String> dataSourceNames = shardingDataSourceRule.getShardingDataSourceNames().getDataSourceNames();
                dataSourceNames = dataSourceNames.stream().filter(name -> slave ? isSlave(name) : isMaster(name)).collect(Collectors.toList());

                ShardingRule shardingRule = new ShardingRule(shardingRuleConfigurationTemp, dataSourceNames);

                dynamicDataSourceMap.put(dsName, new ShardingDataSource(shardingDataSourceMapTemp, shardingRule, shardingRuntimeContext.getProperties().getProps()));
            }
        }
    }

    private boolean isSlave(String name) {
        return name.contains(ShardingJdbcConstants.SLAVE_KEY);
    }

    private boolean isMaster(String name) {
        return !name.contains(ShardingJdbcConstants.SLAVE_KEY);
    }

    @Primary
    @Bean
    @DependsOn("smartDynamicDataSourceProvider")
    public DataSource dataSource(final DynamicDataSourceProvider smartDynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();

        // 如果动态数据源没有配置，则primary设置为sharding jdbc master的数据源
        Map<String, DataSourceProperty> dataSourcePropertyMap = dynamicDataSourceProperties.getDatasource();
        String primary = dynamicDataSourceProperties.getPrimary();
        if (dataSourcePropertyMap == null || dataSourcePropertyMap.size() == 0) {
            primary = ShardingJdbcDS.MASTER;
        }
        dataSource.setPrimary(primary);

        dataSource.setStrict(dynamicDataSourceProperties.getStrict());
        dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
        dataSource.setProvider(smartDynamicDataSourceProvider);
        dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
        dataSource.setSeata(dynamicDataSourceProperties.getSeata());
        return dataSource;
    }

}