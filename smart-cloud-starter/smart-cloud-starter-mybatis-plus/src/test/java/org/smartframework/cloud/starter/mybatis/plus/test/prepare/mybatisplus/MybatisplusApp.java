package org.smartframework.cloud.starter.mybatis.plus.test.prepare.mybatisplus;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SmartBootApplication
@EnableTransactionManagement
public class MybatisplusApp {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusApp.class, args);
    }

}