package org.smartframework.cloud.starter.rabbitmq.test.prepare;

import org.smartframework.cloud.starter.core.support.annotation.SmartBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * 启动类
 *
 * @author collin
 * @date 2021-07-01
 */
@SmartBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}