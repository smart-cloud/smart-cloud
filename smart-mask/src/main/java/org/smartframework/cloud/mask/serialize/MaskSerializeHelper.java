package org.smartframework.cloud.mask.serialize;

import java.lang.reflect.Field;

import org.smartframework.cloud.mask.MaskLog;

public final class MaskSerializeHelper {

	private MaskSerializeHelper() {
	}

	/**
	 * 判断是否需要mask
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isNeedMask(Field field) {
		return String.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(MaskLog.class);
	}

}