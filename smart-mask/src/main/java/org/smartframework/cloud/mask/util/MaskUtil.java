package org.smartframework.cloud.mask.util;

import java.lang.reflect.TypeVariable;

import org.smartframework.cloud.mask.EnableMask;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.serialize.MaskSerializeConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liyulin
 * @desc mask工具类
 * @date 2019-11-05
 */
public final class MaskUtil {

	private static final Cache<Class<?>, MaskTypeEnum> MASK_CLASS_CACHE = CacheBuilder.newBuilder()
			// 设置并发级别
			.concurrencyLevel(Runtime.getRuntime().availableProcessors() << 1)
			// 设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
			.maximumSize(1024 * 1024 * 16)
			// 使用弱引用
			.weakValues().build();

	private MaskUtil() {
	}

	/**
	 * 根据{@link MaskRule}进行截取替换
	 *
	 * @param s
	 * @param maskRule
	 * @return
	 */
	public static String mask(String s, MaskRule maskRule) {
		return mask(s, maskRule.getStartLen(), maskRule.getEndLen(), maskRule.getMask());
	}

	/**
	 * 根据传入的mask规则，替换字符串
	 *
	 * @param obj
	 * @param start
	 * @param end
	 * @param mask
	 * @return
	 */
	public static String mask(Object obj, int start, int end, String mask) {
		if (obj == null) {
			return mask;
		}
		return mask(obj.toString(), start, end, mask);
	}

	/**
	 * 根据传入的mask规则，替换字符串
	 *
	 * @param s
	 * @param start
	 * @param end
	 * @param mask
	 * @return
	 */
	public static String mask(String s, int start, int end, String mask) {
		if (s == null || s.length() == 0) {
			return mask;
		}
		int len = s.length();
		if (len <= start) {
			return mask;
		}
		if (len <= start + end) {
			return s.substring(0, start) + mask;
		}

		return s.substring(0, start) + mask + s.substring(len - end);
	}

	/**
	 * 对敏感数据进行脱敏
	 *
	 * @param object
	 * @return
	 */
	public static String mask(Object object) {
		// -------------------注意-------------------
		// 1、使用SerializerFeature时，不能禁用“循环引用检测”
		// 2、要使用IgnoreNonFieldGetter，否则某些情况会出现NPE
		// ------------------------------------------
		return JSON.toJSONString(object, MaskSerializeConfig.GLOBAL_INSTANCE, SerializerFeature.IgnoreNonFieldGetter);
	}

	/**
	 * 将对象中需要打掩码的单独打掩码（不支持泛型）
	 *
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T wrapMask(T t) {
		if (t == null) {
			return null;
		}

		String mask = MaskUtil.mask(t);
		return (T) JSON.parseObject(mask, t.getClass());
	}

	/**
	 * 是否需要mask
	 *
	 * @param clazz
	 * @return
	 */
	public static MaskTypeEnum getMaskType(Class<?> clazz) {
		MaskTypeEnum maskTypeEnum = MASK_CLASS_CACHE.getIfPresent(clazz);
		if (maskTypeEnum != null) {
			return maskTypeEnum;
		}

		boolean needMask = clazz.isAnnotationPresent(EnableMask.class);
		if (!needMask) {
			maskTypeEnum = MaskTypeEnum.NONE;
		} else {
			TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
			maskTypeEnum = (typeVariables == null || typeVariables.length == 0) ? MaskTypeEnum.NORMAL
					: MaskTypeEnum.GENERIC;
		}
		MASK_CLASS_CACHE.put(clazz, maskTypeEnum);
		return maskTypeEnum;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static enum MaskTypeEnum {
		/** 不需要mask */
		NONE(1),
		/** 需要mask的普通对象（非泛型对象） */
		NORMAL(2),
		/** 需要mask的泛型对象 */
		GENERIC(3);
		private int type;
	}

}