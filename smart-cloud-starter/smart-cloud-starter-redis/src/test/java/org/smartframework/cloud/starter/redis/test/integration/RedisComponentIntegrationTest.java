package org.smartframework.cloud.starter.redis.test.integration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisComponentIntegrationTest extends TestCase {

	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Before
	public void beforeTest() {
		Set<String> keys = stringRedisTemplate.keys("*");
		stringRedisTemplate.delete(keys);
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
	public void testBatchSetString() {
		List<String> keys = Arrays.asList("batchkey1", "batchkey2");
		List<String> values = Arrays.asList("batchvalue1", "batchvalue2");
		Boolean result = redisComponent.batchSetString(keys, values, 1000 * 60L);
		Assertions.assertThat(result).isTrue();

		Assertions.assertThat(redisComponent.getString(keys.get(0))).isEqualTo(values.get(0));
		Assertions.assertThat(redisComponent.getString(keys.get(1))).isEqualTo(values.get(1));
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