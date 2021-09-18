package org.smartframework.cloud.starter.mp.shardingjdbc.autoconfigure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.smartframework.cloud.starter.mp.shardingjdbc.condition.DynamicRoutingDataSourceCondition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 动态数据源配置
 *
 * @author collin
 * @date 2021-09-01
 * @see DynamicDataSourceAutoConfiguration#dataSource()
 */
@Configuration
@AutoConfigureAfter(ShardingjdbcDynamicDataSourceProviderAutoConfiguration.class)
@Conditional(DynamicRoutingDataSourceCondition.class)
public class DynamicRoutingDataSourceAutoConfiguration {

    /**
     * 核心动态数据源组件
     *
     * @param properties 动态数据源配置属性
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DynamicRoutingDataSource dataSource(final DynamicDataSourceProperties properties) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

}