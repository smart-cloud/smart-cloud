/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.web.aspect;

import io.github.smart.cloud.common.pojo.Base;
import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.starter.web.exception.ExceptionHandlerContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 *
 * @author collin
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
	public Response<Base> handleException(Exception e) {
		log.error("global.error", e);

		return new Response<>(ExceptionHandlerContext.transRespHead(e));
	}

}