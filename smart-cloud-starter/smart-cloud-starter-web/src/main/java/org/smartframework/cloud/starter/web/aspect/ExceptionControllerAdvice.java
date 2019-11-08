package org.smartframework.cloud.starter.web.aspect;

import org.smartframework.cloud.common.pojo.dto.BaseDto;
import org.smartframework.cloud.common.pojo.dto.Resp;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.starter.common.business.util.exception.ExceptionHandlerContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 *
 * @author liyulin
 * @date 2019年4月8日下午9:05:25
 */
@Configuration
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Resp<BaseDto> handleException(Exception e) {
		log.error("global.error", e);

		RespHead head = ExceptionHandlerContext.transRespHead(e);
		return new Resp<>(head);
	}

}