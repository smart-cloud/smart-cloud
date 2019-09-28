package org.smartframework.cloud.starter.mybatis.test.cases;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import junit.framework.TestCase;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ShardingJdbcTest extends TestCase {

	@Test
	public void test() {
		try (ConfigurableApplicationContext context = SpringApplication.run(getClass(),
				"--spring.profiles.active=sharding-jdbc");) {
			ShardingDataSource shardingDataSource = context.getBean(ShardingDataSource.class);
			Assertions.assertThat(shardingDataSource).isNotNull();
		}
	}

}