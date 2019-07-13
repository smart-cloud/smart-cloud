package org.smartframework.cloud.starter.log.util;

import org.apache.logging.log4j.message.ParameterizedMessage;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志打印封装，超过指定长度，截取掉
 *
 * @author liyulin
 * @date 2019-03-23
 */
@Slf4j
@UtilityClass
public class LogUtil {

	/** 日志最大长度 */
	private static final int LOG_MAX_LENGTH = 2048;

	public static void trace(String msg) {
		log.trace(truncate(msg));
	}

	public static void trace(String format, Object... args) {
		log.trace(truncate(format, args));
	}

	public static void debug(String msg) {
		log.debug(truncate(msg));
	}

	public static void debug(String format, Object... args) {
		log.debug(truncate(format, args));
	}

	public static void info(String msg) {
		log.info(truncate(msg));
	}

	public static void info(String format, Object... args) {
		log.info(truncate(format, args));
	}

	public static void warn(String msg) {
		log.warn(truncate(msg));
	}

	public static void warn(String format, Object... args) {
		log.warn(truncate(format, args));
	}

	public void warn(String msg, Throwable t) {
		log.warn(msg, t);
	}

	public static void error(String msg) {
		log.error(truncate(msg));
	}

	public static void error(String format, Object... args) {
		log.error(format, args);
	}

	public void error(String msg, Throwable t) {
		log.error(msg, t);
	}

	public static void trace(String msg, int maxLength) {
		log.trace(truncate(msg, maxLength));
	}

	public static void trace(int maxLength, String format, Object... args) {
		log.trace(truncate(maxLength, format, args));
	}

	public static void debug(String msg, int maxLength) {
		log.debug(truncate(msg, maxLength));
	}

	public static void debug(int maxLength, String format, Object... args) {
		log.debug(truncate(maxLength, format, args));
	}

	public static void info(String msg, int maxLength) {
		log.info(truncate(msg, maxLength));
	}

	public static void info(int maxLength, String format, Object... args) {
		log.info(truncate(maxLength, format, args));
	}

	public static void warn(String msg, int maxLength) {
		log.warn(truncate(msg, maxLength));
	}

	public static void warn(int maxLength, String format, Object... args) {
		log.warn(truncate(maxLength, format, args));
	}

	public static void error(String msg, int maxLength) {
		log.error(truncate(msg, maxLength));
	}

	public static void error(int maxLength, String format, Object... args) {
		log.error(truncate(maxLength, format, args));
	}

	private static String truncate(String format, Object... args) {
		String msg = ParameterizedMessage.format(format, args);
		return truncate(msg, LOG_MAX_LENGTH);
	}

	private static String truncate(String msg) {
		return truncate(msg, LOG_MAX_LENGTH);
	}

	private static String truncate(int maxLength, String format, Object... args) {
		String msg = ParameterizedMessage.format(format, args);
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
            final int ix = maxWidth > str.length() ? str.length() : maxWidth;
            return str.substring(0, ix);
        }
        return str.substring(0);
    }
	
}