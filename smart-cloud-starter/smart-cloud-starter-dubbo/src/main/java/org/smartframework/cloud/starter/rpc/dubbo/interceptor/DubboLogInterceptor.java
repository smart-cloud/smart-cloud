package org.smartframework.cloud.starter.rpc.dubbo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.common.web.util.WebUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.dubbo.pojo.DubboLogAspectDO;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author liyulin
 * @desc dubbo rpc接口日志切面
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
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            logDO.setReqEndTime(new Date());
            logDO.setReqDealTime((int) (logDO.getReqEndTime().getTime() - logDO.getReqStartTime().getTime()));
            logDO.setRespData(result);

            // 3、打印日志
            log.info(LogUtil.truncate("rpc.logDO=>{}", logDO));
        }

        return result;
    }

}