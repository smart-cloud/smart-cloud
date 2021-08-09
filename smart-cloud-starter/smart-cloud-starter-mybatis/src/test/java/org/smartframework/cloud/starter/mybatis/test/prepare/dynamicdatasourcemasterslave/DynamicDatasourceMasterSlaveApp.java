package org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasourcemasterslave;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class DynamicDatasourceMasterSlaveApp {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceMasterSlaveApp.class, args);
    }

}