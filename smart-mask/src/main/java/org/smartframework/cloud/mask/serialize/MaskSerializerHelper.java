package org.smartframework.cloud.mask.serialize;

import java.lang.reflect.Field;

import org.smartframework.cloud.mask.MaskLog;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @desc Mask Serializer帮助类
 * @author liyulin
 * @date 2019/11/06
 */
public final class MaskSerializerHelper {

	/** Object类型名 */
	private static final String OBJECT_TYPE_NAME = Object.class.getTypeName();
	/** String类型名 */
	private static final String STRING_TYPE_NAME = String.class.getTypeName();
	private static final Cache<String, Boolean> MASK_MARKER_CACHE = CacheBuilder.newBuilder()
			// 设置并发级别
			.concurrencyLevel(Runtime.getRuntime().availableProcessors() << 1)
			// 设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
			.maximumSize(1024 * 1024 * 16)
			// 使用弱引用
			.weakValues().build();

	private MaskSerializerHelper() {
	}

	public static void cacheMaskBoolean(String typeName, boolean needMask) {
		MASK_MARKER_CACHE.put(typeName, needMask);
	}

	public static Boolean getMaskBooleanFromCache(String typeName) {
		return MASK_MARKER_CACHE.getIfPresent(typeName);
	}

	/**
	 * 是否需要mask
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isNeedMask(Class<?> clazz) {
		while (clazz != null && !OBJECT_TYPE_NAME.equals(clazz.getTypeName())) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (STRING_TYPE_NAME.equals(field.getType().getName())) {
					if (field.isAnnotationPresent(MaskLog.class)) {
						return true;
					}
				} else {
					boolean isNeedMask = isNeedMask(field.getType());
					if (isNeedMask) {
						return true;
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return false;
	}

	/**
	 * 是否需要mask
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isNeedMask(Field field) {
		return field.isAnnotationPresent(MaskLog.class) && STRING_TYPE_NAME.equals(field.getType().getName());
	}

}