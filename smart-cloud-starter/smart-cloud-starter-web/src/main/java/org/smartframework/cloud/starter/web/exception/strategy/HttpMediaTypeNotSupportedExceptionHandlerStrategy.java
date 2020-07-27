package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.springframework.web.HttpMediaTypeNotSupportedException;

/**
 * @desc http请求数据格式不支持异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class HttpMediaTypeNotSupportedExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof HttpMediaTypeNotSupportedException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		return RespHeadUtil.ofI18n(ReturnCodeEnum.UNSUPPORTED_MEDIA_TYPE);
	}

}