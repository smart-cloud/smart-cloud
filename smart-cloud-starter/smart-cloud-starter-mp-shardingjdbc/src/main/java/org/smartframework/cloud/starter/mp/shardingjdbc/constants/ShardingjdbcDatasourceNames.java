package org.smartframework.cloud.starter.mp.shardingjdbc.constants;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;

/**
 * sharding jdbc数据源名称
 *
 * @author collin
 * @date 2021-09-08
 * @see SpringBootConfiguration#shardingDataSource()
 * @see SpringBootConfiguration#masterSlaveDataSource()
 * @see SpringBootConfiguration#encryptDataSource()
 * @see SpringBootConfiguration#shadowDataSource()
 */
public interface ShardingjdbcDatasourceNames {

    /**
     * 分片数据源
     */
    String SHARDING_DATASOURCE = "shardingDataSource";
    /**
     * 主从数据源
     */
    String MASTER_SLAVE_DATASOURCE = "masterSlaveDataSource";
    /**
     * 脱敏数据源
     */
    String ENCRYPT_DATASOURCE = "encryptDataSource";
    /**
     * 影子数据源
     */
    String SHADOW_DATASOURCE = "shadowDataSource";

}