package org.smartframework.cloud.utility;

import lombok.experimental.UtilityClass;

/**
 * 数组工具类
 *
 * @author liyulin
 * @date 2019-04-08
 */
@UtilityClass
public class ArrayUtil {

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断数组是否不为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNotEmpty(Object[] array) {
		return array != null && array.length > 0;
	}

}