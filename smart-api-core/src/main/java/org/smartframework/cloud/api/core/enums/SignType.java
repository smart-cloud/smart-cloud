package org.smartframework.cloud.api.core.enums;

import lombok.Getter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @desc 签名类型
 * @author liyulin
 * @date 2020/04/13
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SignType {

	/** 不需要签名 */
	NONE((byte) 1),
	/** 只有请求参数需要验签 */
	REQUEST((byte) 2),
	/** 只有响应参数需要签名 */
	RESPONSE((byte) 3),
	/** 请求参数需要验签，且响应参数需要签名 */
	ALL((byte) 4);

	/** 签名类型 */
	private byte type;
}