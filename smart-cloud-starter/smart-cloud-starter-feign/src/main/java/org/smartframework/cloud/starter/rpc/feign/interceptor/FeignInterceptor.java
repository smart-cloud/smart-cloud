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
import org.smartframework.cloud.starter.rpc.feign.pojo.FeignLogAspectDO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * feign切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
@Slf4j
public class FeignInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		// 1、填充head
		// TODO:填充token、sign

		FeignLogAspectDO logDO = new FeignLogAspectDO();
		logDO.setReqStartTime(new Date());

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		Method method = invocation.getMethod();
		String apiDesc = AspectInterceptorUtil.getFeignMethodDesc(method, request.getServletPath());
		logDO.setApiDesc(apiDesc);

		String classMethod = method.getDeclaringClass().getTypeName() + SymbolConstant.DOT + method.getName();
		logDO.setClassMethod(classMethod);

		logDO.setReqParams(WebUtil.getRequestArgs(args));
		logDO.setReqHttpHeaders(ReqHttpHeadersUtil.getReqHttpHeadersDto());

		// 2、rpc
		Object result = invocation.proceed();

		logDO.setReqEndTime(new Date());
		logDO.setReqDealTime((int) (logDO.getReqEndTime().getTime() - logDO.getReqStartTime().getTime()));
		logDO.setRespData(result);

		// 3、打印日志
		log.info(LogUtil.truncate("rpc.logDO=>{}", logDO));

		return result;
	}

}