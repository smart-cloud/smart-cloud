package org.smartframework.cloud.starter.rpc.feign.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.starter.common.business.security.util.ReqHttpHeadersUtil;
import org.smartframework.cloud.starter.common.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.common.business.util.WebUtil;
import org.smartframework.cloud.starter.common.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.feign.dto.FeignAspectDto;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * feign切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
public class FeignInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		// 1、填充head
		// TODO:填充token、sign

		FeignAspectDto logDto = new FeignAspectDto();
		logDto.setReqStartTime(new Date());

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		Method method = invocation.getMethod();
		String apiDesc = AspectInterceptorUtil.getFeignMethodDesc(method, request.getServletPath());
		logDto.setApiDesc(apiDesc);

		String classMethod = method.getDeclaringClass().getTypeName() + SymbolConstant.DOT + method.getName();
		logDto.setClassMethod(classMethod);

		logDto.setReqParams(WebUtil.getRequestArgs(args));
		logDto.setReqHttpHeaders(ReqHttpHeadersUtil.getReqHttpHeadersDto());

		// 2、rpc
		Object result = invocation.proceed();

		logDto.setReqEndTime(new Date());
		logDto.setReqDealTime((int) (logDto.getReqEndTime().getTime() - logDto.getReqStartTime().getTime()));
		logDto.setRespData(result);

		// 3、打印日志
		LogUtil.info("rpc.logDto=>{}", logDto);

		return result;
	}

}