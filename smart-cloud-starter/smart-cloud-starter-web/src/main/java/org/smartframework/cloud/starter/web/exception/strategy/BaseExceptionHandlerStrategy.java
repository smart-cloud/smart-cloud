package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.exception.BaseException;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.utility.spring.I18NUtil;

/**
 * @desc 自定义异常传唤
 * @author liyulin
 * @date 2019/10/29
 */
public class BaseExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof BaseException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		BaseException ex = (BaseException) e;
		return RespHeadUtil.of(ex.getCode(), I18NUtil.getMessage(ex.getMessage()));
	}

}