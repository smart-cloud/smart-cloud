package org.smartframework.cloud.utility;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.smartframework.cloud.utility.constant.DateFormartConst;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 时间日期工具类
 *
 * @author liyulin
 * @date 2019-03-23
 */
@Slf4j
@UtilityClass
public class DateUtil {

	/** 所有的日期格式 */
	private static final Map<Integer, String> DATETIME_FORMATTER_ROUTE = new HashMap<>();

	static {
		// 所有公有
		Field[] fields = DateFormartConst.class.getFields();
		// 日期格式变量前缀
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String format = null;
			try {
				format = String.valueOf(field.get(field));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.error(e.getMessage(), e);
			}
			DATETIME_FORMATTER_ROUTE.put(format.length(), format);
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static long currentMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取当前日期（yyyy-MM-dd）
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormartConst.DATE));
	}

	/**
	 * 获取当前日期（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormartConst.DATETIME));
	}

	/**
	 * 获取当前日期
	 * 
	 * @param format 格式
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 格式化日期
	 * 
	 * @param currentMillis
	 * @param format
	 * @return
	 */
	public static String format(long currentMillis, String format) {
		return format(toDate(currentMillis), format);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return 返回格式：yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		return format(date, DateFormartConst.DATE);
	}

	/**
	 * 格式化日期
	 * 
	 * @param currentMillis
	 * @return 返回格式：yyyy-MM-dd
	 */
	public static String formatDate(long currentMillis) {
		return format(toDate(currentMillis), DateFormartConst.DATE);
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return 返回格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(Date date) {
		return format(date, DateFormartConst.DATETIME);
	}

	/**
	 * 格式化日期
	 * 
	 * @param currentMillis
	 * @return 返回格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(long currentMillis) {
		return format(toDate(currentMillis), DateFormartConst.DATETIME);
	}

	/**
	 * 日期字符串转Date对象
	 * 
	 * <h3>支持的字符串格式（{@link DateFormartConst}）</h3>
	 * <ul>
	 * <li>yyyy
	 * <li>yyyy-MM
	 * <li>yyyy-MM-dd
	 * <li>yyyy-MM-dd HH
	 * <li>yyyy-MM-dd HH:mm
	 * <li>yyyy-MM-dd HH:mm:ss
	 * </ul>
	 * 
	 * @param dateStr 日期字符串
	 * @return
	 */
	public static Date toDate(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		int length = dateStr.length();
		String format = DATETIME_FORMATTER_ROUTE.get(length);
		if (Objects.isNull(format)) {
			throw new IllegalArgumentException(String.format("The format of [%s] is not supported！", format));
		}

		Date date = null;
		try {
			date = DateUtils.parseDate(dateStr, format);
		} catch (ParseException e) {
			log.error("data string parse error", e);
		}
		return date;
	}

	/**
	 * 日期字符串转Date对象
	 * 
	 * <h3>支持的字符串格式（{@link DateFormartConst}）</h3>
	 * <ul>
	 * <li>yyyy
	 * <li>yyyy-MM
	 * <li>yyyy-MM-dd
	 * <li>yyyy-MM-dd HH
	 * <li>yyyy-MM-dd HH:mm
	 * <li>yyyy-MM-dd HH:mm:ss
	 * </ul>
	 * 
	 * @param dateStr 日期字符串
	 * @return
	 */
	public static Long toCurrentMillis(String dateStr) {
		Date date = toDate(dateStr);
		if (date == null) {
			return null;
		}
		return date.getTime();
	}

	/**
	 * 时间戳转Date
	 * 
	 * @param currentMillis
	 * @return
	 */
	public static Date toDate(long currentMillis) {
		return new Date(currentMillis);
	}

}