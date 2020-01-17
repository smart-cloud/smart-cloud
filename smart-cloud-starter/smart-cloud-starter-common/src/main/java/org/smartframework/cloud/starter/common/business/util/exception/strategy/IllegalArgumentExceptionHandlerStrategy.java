package org.smartframework.cloud.starter.common.business.util.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.common.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.common.business.util.exception.IExceptionHandlerStrategy;

/**
 * @desc 参数不合法异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class IllegalArgumentExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof IllegalArgumentException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
	}

}