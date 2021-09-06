package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcdynamicdatasource;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class ShardingJdbcDynamicDatasourceApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcDynamicDatasourceApp.class, args);
    }

}