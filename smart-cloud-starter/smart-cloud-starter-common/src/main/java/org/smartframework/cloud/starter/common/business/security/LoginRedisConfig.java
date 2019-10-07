package org.smartframework.cloud.starter.common.business.security;

import java.util.Objects;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoginRedisConfig {

	/**
	 * token redis key
	 * 
	 * @param token
	 * @return
	 */
	public static String getTokenRedisKey(String token) {
		return "mall:user:login:token:" + token;
	}

	/**
	 * userId redis key
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUserIdRedisKey(Long userId) {
		String keyPrefix = "mall:user:login:userId:";
		if (Objects.isNull(userId)) {
			return keyPrefix;
		}
		
		return keyPrefix + userId;
	}

}