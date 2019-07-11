package org.smartframework.cloud.starter.common.business.util;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.starter.common.business.exception.BaseException;
import org.smartframework.cloud.starter.common.constants.SymbolConstant;
import org.smartframework.cloud.utility.CollectionUtil;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.experimental.UtilityClass;

/**
 * 异常工具类
 *
 * @author liyulin
 * @date 2019年4月9日下午6:04:40
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
				errorMsg.append(constraintViolation.getPropertyPath().toString())
						.append(SymbolConstant.HYPHEN)
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

	/**
	 * 将{@link Throwable}解析构造{@link RespHead}
	 * 
	 * @param e
	 * @return
	 */
	public RespHead parse(Throwable e) {
		if (e instanceof BindException) {
			// 参数校验
			BindException bindException = (BindException) e;
			List<FieldError> fieldErrors  = bindException.getFieldErrors();
			if (CollectionUtil.isNotEmpty(fieldErrors)) {
				String errorMsg = getErrorMsg(fieldErrors);
				return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
			}
		}
		if (e instanceof ConstraintViolationException) {
			// 参数校验
			ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
			Set<ConstraintViolation<?>> constraintViolationSet = constraintViolationException.getConstraintViolations();
			if (CollectionUtil.isNotEmpty(constraintViolationSet)) {
				String errorMsg = getErrorMsg(constraintViolationSet);
				return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
			}
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
		}
		if (e instanceof MethodArgumentNotValidException) {
			// 参数校验
			List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
			if (CollectionUtil.isNotEmpty(fieldErrors)) {
				String errorMsg = getErrorMsg(fieldErrors);
				return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, errorMsg);
			}
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
		}
		if (e instanceof IllegalArgumentException) {
			return RespHeadUtil.of(ReturnCodeEnum.VALIDATE_FAIL, e.getMessage());
		}
		if (e instanceof HttpRequestMethodNotSupportedException) {
			return RespHeadUtil.of(ReturnCodeEnum.REQUEST_METHOD_NOT_SUPPORTED, e.getMessage());
		}
		if (e instanceof HttpMediaTypeNotSupportedException) {
			return RespHeadUtil.of(ReturnCodeEnum.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
		}
		if (e instanceof MaxUploadSizeExceededException) {
			return RespHeadUtil.of(ReturnCodeEnum.UPLOAD_FILE_SIZE_EXCEEDED, e.getMessage());
		}
		if (e instanceof NoHandlerFoundException) {
			return RespHeadUtil.of(ReturnCodeEnum.REQUEST_URL_ERROR, e.getMessage());
		}
		if (e instanceof BaseException) {
			BaseException ex = (BaseException) e;
			return RespHeadUtil.of(ex.getCode(), ex.getMessage());
		}
		if (e != null) {
			String message = e.getMessage();
			if (StringUtils.isBlank(message)) {
				message = e.toString();
				// 只取异常类名
				int index = message.lastIndexOf(SymbolConstant.DOT);
				if (index != -1) {
					message = message.substring(index + 1);
				}
			}

			return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, message);
		}

		return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, null);
	}

}