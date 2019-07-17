package org.smartframework.cloud.starter.mybatis.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import junit.framework.TestCase;

@SpringBootApplication(exclude = RedissonAutoConfiguration.class)
public class MybatisPlusTest extends TestCase {

	@Test
	public void test() {
		ConfigurableApplicationContext context = SpringApplication.run(MybatisPlusTest.class, "--spring.profiles.active=mybatisplus");
		MybatisSqlSessionFactoryBean sqlSessionFactoryBean = context.getBean(MybatisSqlSessionFactoryBean.class);
		Assertions.assertThat(sqlSessionFactoryBean).isNotNull();
		context.close();
	}

}