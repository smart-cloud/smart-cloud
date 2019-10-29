package org.smartframework.cloud.starter.common.business.util.exception;

import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.starter.common.business.util.RespHeadUtil;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @desc http请求方式不支持异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class HttpRequestMethodNotSupportedExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof HttpRequestMethodNotSupportedException;
	}

	@Override
	public RespHead transRespHead(Throwable e) {
		return RespHeadUtil.of(ReturnCodeEnum.REQUEST_METHOD_NOT_SUPPORTED, e.getMessage());
	}

}