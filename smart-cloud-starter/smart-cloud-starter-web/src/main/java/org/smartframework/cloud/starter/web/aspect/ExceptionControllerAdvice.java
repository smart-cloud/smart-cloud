package org.smartframework.cloud.starter.web.aspect;

import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.vo.RespVO;
import org.smartframework.cloud.starter.web.exception.ExceptionHandlerContext;
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

	static {
		// 增加对404等的处理
		System.setProperty("spring.mvc.throw-exception-if-no-handler-found","true");
		System.setProperty("spring.resources.add-mappings","false");
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RespVO<Base> handleException(Exception e) {
		log.error("global.error", e);

		return new RespVO<>(ExceptionHandlerContext.transRespHead(e));
	}

}