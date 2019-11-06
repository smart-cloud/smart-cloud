package org.smartframework.cloud.mask.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.smartframework.cloud.mask.MaskConstants;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.serialize.MaskSerializeConfig;
import org.smartframework.cloud.mask.serialize.MaskSerializerHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc mask工具类
 * @author liyulin
 * @date 2019-11-05
 */
@Slf4j
public final class MaskUtil {

	private static final SerializerFeature[] SERIALIZER_FEATURES = { SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteBigDecimalAsPlain,
			SerializerFeature.WriteEnumUsingToString,
			// 禁用“循环引用检测”
			SerializerFeature.DisableCircularReferenceDetect };

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

	public static String mask(String s, MaskLog maskLog) {
		int startLen, endLen;
		String mask;
		if (isSetMaskAttributes(maskLog)) {
			startLen = maskLog.startLen();
			endLen = maskLog.endLen();
			mask = maskLog.mask();
		} else {
			MaskRule maskRule = maskLog.value();
			startLen = maskRule.getStartLen();
			endLen = maskRule.getEndLen();
			mask = maskRule.getMask();
		}
		return MaskUtil.mask(s, startLen, endLen, mask);
	}

	/**
	 * 是否已设置{@link MaskLog}的非规则属性（除了value以外的）
	 * 
	 * @param maskLog
	 * @return
	 */
	private static boolean isSetMaskAttributes(MaskLog maskLog) {
		return maskLog.startLen() != MaskConstants.START_LEN || maskLog.endLen() != MaskConstants.END_LEN
				|| !MaskConstants.DEFAULT_MASK_TEXT.equals(maskLog.mask());
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
		return JSON.toJSONString(object, MaskSerializeConfig.GLOBAL_INSTANCE, SERIALIZER_FEATURES);
	}

	/**
	 * 对敏感数据进行脱敏
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T wrapMask(T t) {
		if (t == null) {
			return t;
		}
		
		Class<?> clazz = t.getClass();
		Boolean needMask = MaskSerializerHelper.getMaskBooleanFromCache(clazz.getTypeName());
		if (needMask == null) {
			needMask = MaskSerializerHelper.isNeedMask(clazz);
			// cache
			MaskSerializerHelper.cacheMaskBoolean(clazz.getTypeName(), needMask);
		}
		if (needMask) {
			// clone
			T cloneBean = null;
			try {
				cloneBean = (T) BeanUtils.cloneBean(t);
			} catch (IllegalAccessException | InstantiationException | InvocationTargetException
					| NoSuchMethodException e) {
				log.error("bean clone error", e);
			}
			// wrap
			if (cloneBean != null) {
				while (clazz != null) {
					Field[] fields = clazz.getDeclaredFields();
					try {
						for (Field field : fields) {
							System.out.println(field.getType());
							if (MaskSerializerHelper.isNeedMask(field)) {
								field.setAccessible(true);
								Object propertyValue = field.get(cloneBean);
								String maskValue = MaskUtil.mask(propertyValue.toString(),
										field.getAnnotation(MaskLog.class));
								field.set(cloneBean, maskValue);
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("get field error by reflection", e);
					}
					clazz = clazz.getSuperclass();
				}
				
				return cloneBean;
			}
		}
		return t;
	}
	
}