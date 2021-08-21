package org.smartframework.cloud.starter.mybatis.plus.constants;

/**
 * sharding jdbc ds
 *
 * @author collin
 * @date 2021-02-25
 */
public interface ShardingJdbcDS {

    /**
     * master
     */
    String MASTER = "sharding";
    /**
     * slave
     */
    String SLAVE = "slaveSharding";

}