package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.fullfunctions;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class ShardingJdbcFullFunctionsApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcFullFunctionsApp.class, args);
    }

}