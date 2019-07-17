package org.smartframework.cloud.starter.mybatis.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import junit.framework.TestCase;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@SpringBootApplication(exclude = RedissonAutoConfiguration.class)
public class MapperTest extends TestCase {

	@Test
	public void test() {
		ConfigurableApplicationContext context = SpringApplication.run(MapperTest.class, "--spring.profiles.active=mapper");
		MapperScannerConfigurer mapperScannerConfigurer = context.getBean(MapperScannerConfigurer.class);
		Assertions.assertThat(mapperScannerConfigurer).isNotNull();
		context.close();
	}

}