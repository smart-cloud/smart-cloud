package org.smartframework.cloud.starter.mybatis.test.cases.masterslave;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MapperMasterSlaveTest extends TestCase {

	@Test
	@Transactional(readOnly = true)
	public void test() {
		try (ConfigurableApplicationContext context = SpringApplication.run(getClass(),
				"--spring.profiles.active=mapper-master-slave");) {
			MapperScannerConfigurer mapperScannerConfigurer = context.getBean(MapperScannerConfigurer.class);
			Assertions.assertThat(mapperScannerConfigurer).isNotNull();
			ApiLogBaseMapper apiLogBaseMapper = context.getBean(ApiLogBaseMapper.class);
			apiLogBaseMapper.selectAll();
		}
	}

}