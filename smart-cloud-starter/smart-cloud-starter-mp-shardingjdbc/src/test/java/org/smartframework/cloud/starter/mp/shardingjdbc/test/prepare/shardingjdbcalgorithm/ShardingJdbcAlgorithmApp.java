package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class ShardingJdbcAlgorithmApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcAlgorithmApp.class, args);
    }

}