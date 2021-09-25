package org.smartframework.cloud.starter.rpc.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.common.web.util.WebUtil;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.feign.pojo.FeignLogAspectDO;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * feign切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
@Slf4j
public class FeignLogInterceptor implements MethodInterceptor, RequestInterceptor, Ordered {

    private static final ThreadLocal<Map<String, Collection<String>>> FEIGN_HEADER_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();

        FeignLogAspectDO logDO = new FeignLogAspectDO();
        Method method = invocation.getMethod();
        String classMethod = method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
        logDO.setClassMethod(classMethod);

        logDO.setReqParams(WebUtil.getRequestArgs(invocation.getArguments()));
        // 2、rpc
        Object result = null;
        try {
            result = invocation.proceed();
            logDO.setRespData(result);
        } finally {
            logDO.setReqHeaders(FEIGN_HEADER_THREAD_LOCAL.get());
            logDO.setCost(System.currentTimeMillis() - startTime);

            // 3、打印日志
            log.info(LogUtil.truncate("rpc.log=>{}", logDO));

            // 方法调用顺序：apply（初始化值） ——> invoke（获取值，并清除）
            // 防止内存泄漏
            FEIGN_HEADER_THREAD_LOCAL.remove();
        }

        return result;
    }

    @Override
    public void apply(RequestTemplate template) {
        FEIGN_HEADER_THREAD_LOCAL.set(template.headers());
    }

    @Override
    public int getOrder() {
        return OrderConstant.FEIGN_LOG;
    }

}