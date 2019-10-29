package org.smartframework.cloud.starter.common.business.util.exception;

import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.starter.common.business.util.RespHeadUtil;

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
	public RespHead transRespHead(Throwable e) {
		return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
	}

}