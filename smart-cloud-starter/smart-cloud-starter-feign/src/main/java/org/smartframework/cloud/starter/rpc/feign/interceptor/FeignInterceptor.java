package org.smartframework.cloud.starter.rpc.feign.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.starter.core.business.SmartReqContext;
import org.smartframework.cloud.starter.core.business.util.AspectInterceptorUtil;
import org.smartframework.cloud.starter.core.business.util.WebUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.feign.pojo.FeignLogAspectDO;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * feign切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
@Slf4j
public class FeignInterceptor implements MethodInterceptor,RequestInterceptor {
	
	private static final ThreadLocal<String> FEIGN_PATH_THREAD_LOCAL = new ThreadLocal<>();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		// 1、填充head
		// TODO:填充token、sign

		FeignLogAspectDO logDO = new FeignLogAspectDO();
		logDO.setReqStartTime(new Date());

		Method method = invocation.getMethod();
		String classMethod = method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
		logDO.setClassMethod(classMethod);

		logDO.setReqParams(WebUtil.getRequestArgs(args));
		logDO.setReqHttpHeaders(SmartReqContext.getReqHttpHeadersBO());

		// 2、rpc
		Object result = invocation.proceed();

		String apiDesc = AspectInterceptorUtil.getFeignMethodDesc(method, FEIGN_PATH_THREAD_LOCAL.get());
		logDO.setApiDesc(apiDesc);
		logDO.setReqEndTime(new Date());
		logDO.setReqDealTime((int) (logDO.getReqEndTime().getTime() - logDO.getReqStartTime().getTime()));
		logDO.setRespData(result);

		// 3、打印日志
		log.info(LogUtil.truncate("rpc.logDO=>{}", logDO));
		
		// 方法调用顺序：apply（初始化值） ——> invoke（获取值，并清除）
		// 防止内存泄漏
		FEIGN_PATH_THREAD_LOCAL.remove();

		return result;
	}

	@Override
	public void apply(RequestTemplate template) {
		FEIGN_PATH_THREAD_LOCAL.set(template.path());
	}

}