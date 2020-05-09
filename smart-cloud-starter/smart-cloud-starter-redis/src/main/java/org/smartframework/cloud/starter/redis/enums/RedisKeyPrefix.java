package org.smartframework.cloud.starter.redis.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc redis key前缀
 * @author liyulin
 * @date 2020/04/15
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RedisKeyPrefix {

	/** redis key分隔符 */
	REDIS_KEY_SEPARATOR(":"),
	/** cache: */
	CACHE("cache" + REDIS_KEY_SEPARATOR.key),
	/** lock: */
	LOCK("lock" + REDIS_KEY_SEPARATOR.key),
	/** data: */
	DATA("data" + REDIS_KEY_SEPARATOR.key);

	private String key;

}