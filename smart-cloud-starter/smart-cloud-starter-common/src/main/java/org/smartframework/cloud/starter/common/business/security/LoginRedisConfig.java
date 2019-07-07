package org.smartframework.cloud.starter.common.business.security;

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
	 * @param userId
	 * @return
	 */
	public static String getUserIdRedisKey(Long userId) {
		return "mall:user:login:userId:" + userId;
	}

}