package org.smartframework.cloud.starter.common.business.exception;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

/**
 * 重复提交校验异常
 * 
 * @author liyulin
 * @date 2019-07-03
 */
public class RepeatSubmitException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public RepeatSubmitException(String message) {
		setCode(ReturnCodeEnum.REPEAT_SUBMIT.getCode());
		setMessage(message);
	}

}