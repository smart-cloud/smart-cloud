package org.smartframework.cloud.starter.core.business.util;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.springframework.validation.FieldError;

import lombok.experimental.UtilityClass;

/**
 * 异常工具类
 *
 * @author liyulin
 * @date 2019-04-09
 */
@UtilityClass
public class ExceptionUtil {

	/** 错误分隔符 */
	private String ERROR_SEPARATOR = " | ";

	/**
	 * 异常信息序列化
	 * 
	 * @param t
	 * @return
	 */
	public String toString(Throwable t) {
		StackTraceElement[] stackTraceElements = t.getStackTrace();
		return t.getClass().getTypeName() + ERROR_SEPARATOR + StringUtils.join(stackTraceElements, ERROR_SEPARATOR);
	}

	public String getErrorMsg(Set<ConstraintViolation<?>> constraintViolationSet) {
		StringBuilder errorMsg = new StringBuilder();
		int size = constraintViolationSet.size();
		int i = 0;
		for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
			if (size > 1) {
				errorMsg.append((++i) + SymbolConstant.DOT);
			}
			if (constraintViolation.getPropertyPath() == null) {
				errorMsg.append(constraintViolation.getMessage());
			} else {
				errorMsg.append(constraintViolation.getPropertyPath().toString()).append(SymbolConstant.HYPHEN)
						.append(constraintViolation.getMessage());
			}
			if (size > 1 && i < size) {
				errorMsg.append("; ");
			}
		}

		return errorMsg.toString();
	}

	public String getErrorMsg(List<FieldError> fieldErrors) {
		StringBuilder errorMsg = new StringBuilder();
		for (int i = 0, size = fieldErrors.size(); i < size; i++) {
			if (size > 1) {
				errorMsg.append((i + 1) + SymbolConstant.DOT);
			}

			String validateField = fieldErrors.get(i).getField();
			errorMsg.append(validateField + SymbolConstant.HYPHEN + fieldErrors.get(i).getDefaultMessage());
			if (size > 1 && i < size - 1) {
				errorMsg.append("; ");
			}
		}

		return errorMsg.toString();
	}

}