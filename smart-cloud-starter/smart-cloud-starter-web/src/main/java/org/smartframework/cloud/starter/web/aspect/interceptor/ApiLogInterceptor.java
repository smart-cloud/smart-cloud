package org.smartframework.cloud.starter.web.aspect.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.common.pojo.dto.Resp;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.starter.common.business.security.util.ReqHttpHeadersUtil;
import org.smartframework.cloud.starter.common.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.common.business.util.ExceptionUtil;
import org.smartframework.cloud.starter.common.business.util.WebUtil;
import org.smartframework.cloud.starter.common.constants.SymbolConstant;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.web.aspect.dto.LogAspectDto;
import org.smartframework.cloud.utility.ObjectUtil;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 接口日志切面
 *
 * @author liyulin
 * @date 2019-04-08
 */
public class ApiLogInterceptor implements MethodInterceptor, Ordered {

	@Override
	public int getOrder() {
		return OrderConstant.API_LOG;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 请求前
		if (ObjectUtil.isNull(RequestContextHolder.getRequestAttributes())) {
			return invocation.proceed();
		}
		LogAspectDto logDto = new LogAspectDto();
		logDto.setReqStartTime(new Date());

		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		Method method = invocation.getMethod();
		String apiDesc = AspectInterceptorUtil.getControllerMethodDesc(method, request.getServletPath());
		logDto.setApiDesc(apiDesc);

		logDto.setReqParams(WebUtil.getRequestArgs(invocation.getArguments()));
		logDto.setReqHttpHeaders(ReqHttpHeadersUtil.getReqHttpHeadersDto());

		logDto.setUrl(request.getRequestURL().toString());
		logDto.setIp(WebUtil.getRealIP(request));
		logDto.setOs(request.getHeader("User-Agent"));
		logDto.setHttpMethod(request.getMethod());

		String classMethod = method.getDeclaringClass().getTypeName() + SymbolConstant.DOT + method.getName();
		logDto.setClassMethod(classMethod);

		// 处理请求
		Object result = null;
		try {
			result = invocation.proceed();
			// 正常请求后
			logDto.setReqEndTime(new Date());
			logDto.setReqDealTime(getReqDealTime(logDto));
			logDto.setRespData(result);

			LogUtil.info("api.logDto.info=>{}", logDto);
			return result;
		} catch (Exception e) {
			logDto.setReqEndTime(new Date());
			logDto.setReqDealTime(getReqDealTime(logDto));
			logDto.setExceptionStackInfo(ExceptionUtil.toString(e));

			LogUtil.error("api.logDto.error=>{}", logDto);

			RespHead head = ExceptionUtil.parse(e);
			return new Resp<>(head);
		}
	}

	private final int getReqDealTime(LogAspectDto logDto) {
		return (int) (logDto.getReqEndTime().getTime() - logDto.getReqStartTime().getTime());
	}

}