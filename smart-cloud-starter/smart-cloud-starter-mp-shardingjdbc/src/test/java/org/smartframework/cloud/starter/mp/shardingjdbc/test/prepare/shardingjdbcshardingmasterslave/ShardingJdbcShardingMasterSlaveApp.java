package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class ShardingJdbcShardingMasterSlaveApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcShardingMasterSlaveApp.class, args);
    }

}