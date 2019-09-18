package org.smartframework.cloud.starter.rpc.dubbo.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.starter.common.business.util.WebUtil;
import org.smartframework.cloud.starter.common.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.dubbo.pojo.DubboLogAspectDO;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc dubbo rpc接口日志切面
 * @author liyulin
 * @date 2019/09/15
 */
@Slf4j
public class DubboLogInterceptor implements MethodInterceptor {
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DubboLogAspectDO logDO = new DubboLogAspectDO();
		logDO.setReqStartTime(new Date());
		
		Method method = invocation.getMethod();
		String classMethod = method.getDeclaringClass().getTypeName() + SymbolConstant.DOT + method.getName();
		logDO.setClassMethod(classMethod);
		logDO.setReqParams(WebUtil.getRequestArgs(invocation.getArguments()));

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