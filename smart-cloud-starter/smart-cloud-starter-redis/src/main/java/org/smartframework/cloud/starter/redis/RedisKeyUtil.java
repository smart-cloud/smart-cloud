package org.smartframework.cloud.starter.redis;

import org.smartframework.cloud.starter.redis.enums.RedisKeyPrefix;

/**
 * redis key 工具类
 *
 * @author liyulin
 * @date 2020/04/15
 */
public final class RedisKeyUtil {

    private RedisKeyUtil() {
    }

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
        // 第一个以“:”开头
        if (args[0].startsWith(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey())) {
            String result = args[0].substring(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey().length(), args[0].length());
            if (args.length == 1) {
                return result;
            }
            args[0] = result;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            boolean thisEndsWithSeparator = args[i].endsWith(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey());
            boolean nextStartWithSeparator = args[i + 1].startsWith(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey());
            if (thisEndsWithSeparator && nextStartWithSeparator) {
                sb = sb.append(
                        args[i].substring(0, args[i].length() - RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey().length()));
            } else if (!thisEndsWithSeparator && !nextStartWithSeparator) {
                sb = sb.append(args[i]).append(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey());
            } else {
                sb = sb.append(args[i]);
            }
        }

        // 最后一个以“:”结尾
        int lastIndex = args.length - 1;
        if (args[lastIndex].endsWith(RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey())) {
            args[lastIndex] = args[lastIndex].substring(0,
                    args[lastIndex].length() - RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey().length());
        }

        sb = sb.append(args[args.length - 1]);
        return sb.toString();
    }

}