package org.smartframework.cloud.mask.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * 日志工具类
 *
 * @author liyulin
 * @date 2019-03-23
 */
public class LogUtil {

    /**
     * 日志最大长度
     */
    private static final int LOG_MAX_LENGTH = 2048;

    private LogUtil() {
    }

    public static String truncate(String format, Object... args) {
        return truncate(LOG_MAX_LENGTH, format, args);
    }

    public static String truncate(String msg) {
        return truncate(msg, LOG_MAX_LENGTH);
    }

    public static String truncate(int maxLength, String format, Object... args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = MaskUtil.mask(args[i]);
            }
        }

        String msg = MessageFormatter.format(format, args).getMessage();
        return truncate(msg, maxLength);
    }

    private static String truncate(final String str, final int maxWidth) {
        if (maxWidth < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        }

        if (str == null) {
            return null;
        }

        if (str.length() > maxWidth) {
            return str.substring(0, maxWidth);
        }

        return str;
    }

}