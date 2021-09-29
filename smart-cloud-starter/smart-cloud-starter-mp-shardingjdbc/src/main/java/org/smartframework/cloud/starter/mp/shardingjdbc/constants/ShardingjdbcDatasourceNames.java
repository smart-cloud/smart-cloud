package org.smartframework.cloud.starter.mp.shardingjdbc.constants;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;

/**
 * sharding jdbc数据源名称
 *
 * @author collin
 * @date 2021-09-08
 * @see ShardingSphereAutoConfiguration#shardingSphereDataSource(ObjectProvider)
 */
public interface ShardingjdbcDatasourceNames {

    /**
     * 分片数据源
     */
    String DATASOURCE = "shardingSphereDataSource";

}