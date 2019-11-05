package org.smartframework.cloud.mask;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc mask规则
 * @author liyulin
 * @date 2019/11/05
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MaskRule {

	/** 手机号 */
	MOBILE(3, 3, MaskConstants.DEFAULT_MASK_TEXT),
	/** 银行卡 */
	BANKCARD(4, 4, MaskConstants.DEFAULT_MASK_TEXT),
	/** 身份证 */
	IDCARD(4, 4, MaskConstants.DEFAULT_MASK_TEXT),
	/** ip地址 */
	IP(3, 0, MaskConstants.DEFAULT_MASK_TEXT),
	/** 名字 */
	NAME(1, 0, MaskConstants.DEFAULT_MASK_TEXT),
	/** 密码 */
	PASSWROD(0, 0, MaskConstants.DEFAULT_MASK_TEXT),
	/** 默认（全部mask） */
	DEFAULT(0, 0, MaskConstants.DEFAULT_MASK_TEXT);

	/** 开头保留的长度 */
	private int startLen;
	/** 结尾保留的长度 */
	private int endLen;
	/** 掩码值 */
	private String mask;

}