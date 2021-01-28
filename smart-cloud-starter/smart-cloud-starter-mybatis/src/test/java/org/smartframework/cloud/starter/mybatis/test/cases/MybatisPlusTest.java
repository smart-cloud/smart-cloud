package org.smartframework.cloud.starter.mybatis.test.cases;

import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MybatisPlusTest {

    @Test
    public void test() {
        PackageConfig.setBasePackages(new String[]{"org.smartframework.cloud.starter.mybatis.test.cases"});

        ConfigurableApplicationContext context = SpringApplication.run(getClass(),
                "--spring.profiles.active=mybatisplus");
    }

}