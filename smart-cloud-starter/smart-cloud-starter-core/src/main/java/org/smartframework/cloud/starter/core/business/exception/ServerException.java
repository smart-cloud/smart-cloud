package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

/**
 * 服务器异常
 *
 * @author liyulin
 * @date 2019-06-29
 */
public class ServerException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ServerException(IBaseReturnCode baseReturnCode) {
		super(baseReturnCode);
	}

	public ServerException(String message) {
		super.setCode(ReturnCodeEnum.SERVER_ERROR.getCode());
		super.setMessage(message);
	}

}