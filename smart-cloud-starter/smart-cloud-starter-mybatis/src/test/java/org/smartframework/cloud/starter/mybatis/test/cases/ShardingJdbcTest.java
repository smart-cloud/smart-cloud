package org.smartframework.cloud.starter.mybatis.test.cases;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import junit.framework.TestCase;

@SpringBootApplication(exclude = RedissonAutoConfiguration.class)
public class ShardingJdbcTest extends TestCase {

	@Test
	public void test() {
		ConfigurableApplicationContext context = SpringApplication.run(ShardingJdbcTest.class, "--spring.profiles.active=sharding-jdbc");
		ShardingDataSource shardingDataSource = context.getBean(ShardingDataSource.class);
		Assertions.assertThat(shardingDataSource).isNotNull();
		context.close();
	}

}