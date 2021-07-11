package org.smartframework.cloud.mask;

/**
 * @desc mask配置
 * @author liyulin
 * @date 2019/11/05
 */
public interface DefaultMaskConfig {

	/** 默认开头保留的长度 */
	int START_LEN = 0;

	/** 默认结尾保留的长度 */
	int END_LEN = 0;

	/** 默认的掩码 */
	String MASK_TEXT = "***";

}