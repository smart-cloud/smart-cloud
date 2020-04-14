package org.smartframework.cloud.starter.core.business.util.exception.strategy;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.ExceptionUtil;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.core.business.util.exception.IExceptionHandlerStrategy;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 * @desc 参数检验异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class BindExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof BindException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		// 参数校验
		BindException bindException = (BindException) e;
		List<FieldError> fieldErrors = bindException.getFieldErrors();
		if (CollectionUtils.isNotEmpty(fieldErrors)) {
			String errorMsg = ExceptionUtil.getErrorMsg(fieldErrors);
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
		}
		return null;
	}

}