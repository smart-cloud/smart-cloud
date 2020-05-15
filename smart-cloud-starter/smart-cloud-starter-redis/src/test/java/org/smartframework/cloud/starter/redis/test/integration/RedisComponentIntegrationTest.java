package org.smartframework.cloud.starter.redis.test.integration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class RedisComponentIntegrationTest extends TestCase {

	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private static RedisServer server = null;
	
	static {
		try {
			server = new RedisServer();
			server.start();
		} catch (IOException e) {
			log.error("embedded redis start error", e);
		}
	}

	@Before
	public void beforeTest() {
		Set<String> keys = stringRedisTemplate.keys("*");
		stringRedisTemplate.delete(keys);
	}

	@AfterClass
	public static void afterTestClass() throws IOException {
		server.stop();
		server = null;
	}

	@Test
	public void testSetString() {
		String key = "SetStringkey";
		String value = "SetStringvalue";
		redisComponent.setString(key, value, null);
		redisComponent.setString(key, value, 1000 * 60L);
		String expectedValue = redisComponent.getString(key);
		Assertions.assertThat(value).isEqualTo(expectedValue);
	}

	@Test
	public void testDelete() {
		String key = "deletekey";
		String value = "deletevalue";
		redisComponent.setString(key, value, null);
		String expectedValue1 = redisComponent.getString(key);
		Assertions.assertThat(value).isEqualTo(expectedValue1);

		Boolean result1 = redisComponent.delete(key);
		Assertions.assertThat(result1).isTrue();

		Boolean result2 = redisComponent.delete(key);
		Assertions.assertThat(result2).isFalse();

		String expectedValue2 = redisComponent.getString(key);
		Assertions.assertThat(expectedValue2).isNull();
	}
	
	@Test
	public void testBatchDelete() {
		String key1 = "batchdeletekey1";
		String value1 = "batchdeletevalue1";
		redisComponent.setString(key1, value1, null);
		String expectedValue1 = redisComponent.getString(key1);
		Assertions.assertThat(value1).isEqualTo(expectedValue1);
		
		String key2 = "batchdeletekey2";
		String value2 = "batchdeletevalue2";
		redisComponent.setString(key2, value2, null);
		String expectedValue2 = redisComponent.getString(key2);
		Assertions.assertThat(value2).isEqualTo(expectedValue2);

		Boolean result1 = redisComponent.delete(Arrays.asList(key1, key2));
		Assertions.assertThat(result1).isTrue();

		Boolean result2 = redisComponent.delete(Arrays.asList(key1, key2));
		Assertions.assertThat(result2).isFalse();

		String expectedValue12 = redisComponent.getString(key1);
		String expectedValue22 = redisComponent.getString(key2);
		Assertions.assertThat(expectedValue12).isNull();
		Assertions.assertThat(expectedValue22).isNull();
	}

	@Test
	public void testSetObject() {
		String key = "SetObjectkey";
		SetObject setObject = new SetObject("test");
		redisComponent.setObject(key, setObject, null);
		SetObject expectedValue = redisComponent.getObject(key, new TypeReference<SetObject>() {
		});
		Assertions.assertThat(setObject.getName()).isEqualTo(expectedValue.getName());
	}

	@Test
	public void testSetNX() {
		String key = "SetNXkey";
		String value = "SetNXvalue";
		Boolean result1 = redisComponent.setNx(key, value, 1000 * 60L);
		Assertions.assertThat(result1).isTrue();

		Boolean result2 = redisComponent.setNx(key, value, 1000 * 60L);
		Assertions.assertThat(result2).isFalse();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	static class SetObject {
		private String name;
	}

}