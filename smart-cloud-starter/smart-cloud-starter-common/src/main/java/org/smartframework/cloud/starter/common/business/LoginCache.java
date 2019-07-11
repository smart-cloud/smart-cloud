package org.smartframework.cloud.starter.common.business;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户登陆缓存信息
 *
 * @author liyulin
 * @date 2019年6月29日下午4:50:26
 */
@Getter
@Setter
public class LoginCache extends BaseDto {

	private static final long serialVersionUID = 1L;

	/** 访问token */
	private String token;
	/** 用户id */
	private Long userId;
	/** aes加密key */
	private String aesKey;
	/** 签名的modulus */
	private String signModules;
	/** 签名的key */
	private String signKey;
	/** 校验签名的modulus */
	private String checkSignModulus;
	/** 校验签名的key */
	private String checkSignKey;

}