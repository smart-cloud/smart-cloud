package org.smartframework.cloud.starter.job.util;

import com.xxl.job.core.log.XxlJobLogger;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * xxl-job日志工具类（会在日志系统、xxl-job中同时写入）
 * 
 * @author liyulin
 * @date 2019-7-11
 */
@UtilityClass
@Slf4j
public class XxlJobLogUtil {

	public static void debug(String format, Object... arguments) {
		log.debug(format, arguments);
		XxlJobLogger.log(format, arguments);
	}

	public static void info(String format, Object... arguments) {
		log.info(format, arguments);
		XxlJobLogger.log(format, arguments);
	}

	public static void warning(String format, Object... arguments) {
		log.warn(format, arguments);
		XxlJobLogger.log(format, arguments);
	}

	public static void error(String msg, Throwable e) {
		log.error(msg, e);
		XxlJobLogger.log(e);
	}

}