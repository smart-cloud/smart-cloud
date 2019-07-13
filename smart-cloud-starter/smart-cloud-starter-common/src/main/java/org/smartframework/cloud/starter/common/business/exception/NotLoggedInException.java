package org.smartframework.cloud.starter.common.business.exception;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

/**
 * 未登陆异常
 *
 * @author liyulin
 * @date 2019-07-06
 */
public class NotLoggedInException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public NotLoggedInException() {
		super(ReturnCodeEnum.NOT_LOGGED_IN);
	}

}