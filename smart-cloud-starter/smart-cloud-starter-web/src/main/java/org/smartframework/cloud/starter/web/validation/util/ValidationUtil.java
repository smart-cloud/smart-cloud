package org.smartframework.cloud.starter.web.validation.util;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.smartframework.cloud.starter.common.business.exception.ParamValidateException;
import org.smartframework.cloud.starter.common.business.util.ExceptionUtil;
import org.smartframework.cloud.starter.web.validation.ValidatorSingleton;
import org.smartframework.cloud.utility.CollectionUtil;

import lombok.experimental.UtilityClass;

/**
 * 参数校验
 *
 * @author liyulin
 * @date 2019-05-01
 */
@UtilityClass
public class ValidationUtil {

	/**
	 * 参数校验
	 * 
	 * @param object
	 */
	public static void validate(Object object) {
		if (object == null) {
			throw new ParamValidateException("待校验参数object不能为null");
		}

		Validator validator = ValidatorSingleton.getInstance();
		Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(object);
		// 抛出检验异常
		if (CollectionUtil.isNotEmpty(constraintViolationSet)) {
			Set<ConstraintViolation<?>> constraintViolationSetTmp = constraintViolationSet.stream()
					.map(item -> (ConstraintViolation<?>) (item)).collect(Collectors.toSet());

			String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSetTmp);
			throw new ParamValidateException(errorMsg);
		}
	}

}