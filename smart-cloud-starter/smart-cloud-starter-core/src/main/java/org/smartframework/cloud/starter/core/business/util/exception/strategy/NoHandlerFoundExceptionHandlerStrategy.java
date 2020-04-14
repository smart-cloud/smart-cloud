package org.smartframework.cloud.starter.core.business.util.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.core.business.util.exception.IExceptionHandlerStrategy;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @desc mapping url找不到异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class NoHandlerFoundExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof NoHandlerFoundException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		return RespHeadUtil.of(ReturnCodeEnum.REQUEST_URL_ERROR, e.getMessage());
	}

}