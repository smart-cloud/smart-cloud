package org.smartframework.cloud.starter.common.business.exception;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

/**
 * 参数校验错误
 *
 * @author liyulin
 * @date 2019年5月1日上午11:56:59
 */
public class ParamValidateException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ParamValidateException(String message) {
		setCode(ReturnCodeEnum.PARAMETERS_MISSING.getCode());
		setMessage(message);
	}

}