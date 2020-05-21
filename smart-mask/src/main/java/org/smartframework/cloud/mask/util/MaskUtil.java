package org.smartframework.cloud.mask.util;

import org.smartframework.cloud.mask.MaskRule;

/**
 * @author liyulin
 * @desc mask工具类
 * @date 2019-11-05
 */
public final class MaskUtil {

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
	public static final String mask(Object object) {
		return JacksonMaskUtil.mask(object);
	}

}