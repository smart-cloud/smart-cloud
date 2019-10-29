package org.smartframework.cloud.starter.common.business.util.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.starter.common.business.util.ExceptionUtil;
import org.smartframework.cloud.starter.common.business.util.RespHeadUtil;

/**
 * @desc 参数校验异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public class ConstraintViolationExceptionHandlerStrategy implements IExceptionHandlerStrategy {

	@Override
	public boolean match(Throwable e) {
		return e instanceof ConstraintViolationException;
	}

	@Override
	public RespHead transRespHead(Throwable e) {
		// 参数校验
		ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
		Set<ConstraintViolation<?>> constraintViolationSet = constraintViolationException.getConstraintViolations();
		if (CollectionUtils.isNotEmpty(constraintViolationSet)) {
			String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSet);
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
		}
		
		return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
	}

}