package org.smartframework.cloud.starter.redis;

/**
 * @desc redis key 工具类
 * @author liyulin
 * @date 2020/04/15
 */
public final class RedisKeyUtil {

	/**
	 * 构建key
	 * 
	 * @param args
	 * @return
	 */
	public static String buildKey(String... args) {
		if (args == null || args.length == 0) {
			throw new UnsupportedOperationException("args can not be null or empty.");
		}
		
		if (args.length == 1) {
			return args[0];
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
//			if(args[i].endsWith(RedisKeyPrefix.REDIS_KEY_SEPARATOR)) {
//				
//			}
		}

		return sb.toString();
	}

}