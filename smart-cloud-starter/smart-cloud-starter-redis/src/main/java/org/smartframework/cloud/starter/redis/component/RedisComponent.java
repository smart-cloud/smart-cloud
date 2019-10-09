package org.smartframework.cloud.starter.redis.component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * redis常用api封装
 *
 * @author liyulin
 * @date 2018-10-17
 */
@Slf4j
@AllArgsConstructor
public class RedisComponent {

	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 设置k-v对
	 * 
	 * @param key
	 * @param value
	 * @param expireMillis 有效期（毫秒）
	 */
	public void setString(String key, String value, long expireMillis) {
		stringRedisTemplate.opsForValue().set(key, value, expireMillis, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 批量设置k-v对
	 * 
	 * @param keys
	 * @param values
	 * @param expireSeconds 有效期（秒）
	 * @return
	 */
	public boolean batchSetString(List<String> keys, List<String> values, long expireSeconds) {
		final RedisResult result = new RedisResult(false);
		stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					connection.multi();
					for (int i = 0; i < keys.size(); i++) {
						connection.setEx(keys.get(i).getBytes(), expireSeconds, values.get(i).getBytes());
					}
					connection.exec();
					result.setBool(true);
				} catch (Exception e) {
					log.error("redis事务失败", e);
					connection.discard();
				}

				return null;
			}
		});

		return result.isBool();
	}
	
	/**
	 * 根据key获取value
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 删除k-v对
	 * 
	 * @param key
	 * @return
	 */
	public Boolean delete(String key) {
		return stringRedisTemplate.delete(key);
	}

	/**
	 * 设置v-k对
	 * 
	 * @param key
	 * @param value 对象
	 * @param expireMillis 有效期（毫秒）
	 */
	public void setObject(String key, Object value, long expireMillis) {
		setString(key, JSON.toJSONString(value), expireMillis);
	}

	/**
	 * 根据key获取Object
	 * 
	 * @param key
	 * @param t
	 * @param <T> 返回对象类型
	 * @return
	 */
	public <T> T getObject(String key, TypeReference<T> t) {
		String value = getString(key);
		if (null == value) {
			return null;
		}

		return JSON.parseObject(value, t);
	}

	/**
	 * 不存在则设置；存在则不设置
	 * 
	 * @param key
	 * @param value
	 * @param expireMillis 有效期（毫秒）
	 * @return {@code true}表示成功；{@code false}表示失败；null when used in pipeline / transaction.
	 */
	public Boolean setNx(String key, String value, long expireMillis) {
		return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.set(key.getBytes(), value.getBytes(), Expiration.milliseconds(expireMillis),
						SetOption.SET_IF_ABSENT);
			}
		}, true);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	class RedisResult{
		private boolean bool;
	}
	
}
