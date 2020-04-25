package org.smartframework.cloud.starter.web.exception.strategy;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @desc 方法参数不合法异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class MethodArgumentNotValidExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof MethodArgumentNotValidException;
	}

	@Override
	public RespHeadVO transRespHead(Throwable e) {
		// 参数校验
		List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
		if (CollectionUtils.isNotEmpty(fieldErrors)) {
			String errorMsg = ExceptionUtil.getErrorMsg(fieldErrors);
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
		}
		return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
	}

}