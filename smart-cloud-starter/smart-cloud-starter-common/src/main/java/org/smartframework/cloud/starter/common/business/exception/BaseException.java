package org.smartframework.cloud.starter.common.business.exception;

import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义异常基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 状态码 */
	private String code;
	/** 提示信息 */
	private String message;
	
	public BaseException(IBaseReturnCode baseReturnCode) {
		setCode(baseReturnCode.getCode());
		setMessage(baseReturnCode.getMessage());
	}

	@Override
	public String toString() {
		return "[" + code + "]:" + message;
	}

}