package org.smartframework.cloud.starter.mybatis.test.cases;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MybatisPlusTest extends TestCase {

    @Test
    public void test() {
        ConfigurableApplicationContext context = SpringApplication.run(getClass(),
                "--spring.profiles.active=mybatisplus");
    }

}