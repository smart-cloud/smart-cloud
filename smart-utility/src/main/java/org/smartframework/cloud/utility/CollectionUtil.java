package org.smartframework.cloud.utility;

import java.util.Collection;
import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 集合工具类
 *
 * @author liyulin
 * @date 2019-03-30
 */
@UtilityClass
public class CollectionUtil {

	/**
	 * 判断<code>Collection</code>是否为空
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * 判断<code>Collection</code>是否不为空
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}

	/**
	 * 判断<code>Map</code>是否为空
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * 判断<code>Map</code>是否不为空
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return (map != null && map.size() > 0);
	}

}