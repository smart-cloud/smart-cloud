package org.smartframework.cloud.utility;

import lombok.experimental.UtilityClass;

/**
 * Object工具类
 *
 * @author liyulin
 * @date 2019-04-07
 */
@UtilityClass
public class ObjectUtil {

	/**
	 * 是否为null
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * 是否不为null
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNotNull(Object object) {
		return object != null;
	}

	/**
	 * objects是否都为null
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean isAllNull(Object... objects) {
		if (objects == null) {
			return true;
		}

		for (Object object : objects) {
			if (object != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * objects是否都不为null
	 * 
	 * @param objects
	 * @return
	 */
	public static boolean isAllNotNull(Object... objects) {
		if (objects == null) {
			return false;
		}

		for (Object object : objects) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断两对象的字符串是否相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

}