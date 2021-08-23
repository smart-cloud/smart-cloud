package org.smartframework.cloud.starter.mp.shardingjdbc.constants;

/**
 * sharding jdbc数据源名称
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