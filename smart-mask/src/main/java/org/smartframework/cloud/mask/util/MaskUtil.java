package org.smartframework.cloud.mask.util;

import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.serialize.MaskSerializeConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.experimental.UtilityClass;

/**
 * @desc mask工具类
 * @author liyulin
 * @date 2019-11-05
 */
@UtilityClass
public class MaskUtil {

	private static final SerializerFeature[] SERIALIZER_FEATURES = { SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteBigDecimalAsPlain,
			SerializerFeature.WriteEnumUsingToString,
			// 禁用“循环引用检测”
			SerializerFeature.DisableCircularReferenceDetect };

	public static String mask(String s, MaskRule maskRule) {
		return mask(s, maskRule.getStartLen(), maskRule.getEndLen(), maskRule.getMask());
	}

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

}