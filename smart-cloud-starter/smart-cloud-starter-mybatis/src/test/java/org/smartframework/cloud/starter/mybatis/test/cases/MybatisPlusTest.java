package org.smartframework.cloud.starter.mybatis.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import junit.framework.TestCase;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MybatisPlusTest extends TestCase {

	@Test
	public void test() {
		try (ConfigurableApplicationContext context = SpringApplication.run(getClass(),
				"--spring.profiles.active=mybatisplus");) {
			MybatisSqlSessionFactoryBean sqlSessionFactoryBean = context.getBean(MybatisSqlSessionFactoryBean.class);
			Assertions.assertThat(sqlSessionFactoryBean).isNotNull();
		}
	}

}