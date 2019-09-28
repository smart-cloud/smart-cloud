package org.smartframework.cloud.starter.mybatis.test.cases;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import junit.framework.TestCase;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MapperTest extends TestCase {

	@Test
	public void test() {
		try (ConfigurableApplicationContext context = SpringApplication.run(getClass(),
				"--spring.profiles.active=mapper");) {
			MapperScannerConfigurer mapperScannerConfigurer = context.getBean(MapperScannerConfigurer.class);
			Assertions.assertThat(mapperScannerConfigurer).isNotNull();
		}
	}

}