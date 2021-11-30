package org.smartframework.cloud.starter.rpc.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.common.web.util.WebUtil;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.mask.util.LogUtil;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.configure.properties.LogProperties;
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
@AllArgsConstructor
public class FeignLogInterceptor implements MethodInterceptor, RequestInterceptor, Ordered {

    private LogProperties logProperties;
    private static final ThreadLocal<Map<String, Collection<String>>> FEIGN_HEADER_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 1、rpc
        Object result = null;
        try {
            result = invocation.proceed();
        } finally {
            // 2、打印日志
            long cost = System.currentTimeMillis() - startTime;
            if (cost >= logProperties.getSlowApiMinCost()) {
                log.warn(LogUtil.truncate("rpc.slow=>{}", buildFeignLogAspectDO(invocation.getMethod(), invocation.getArguments(), result, cost)));
            } else if (log.isInfoEnabled()) {
                log.info(LogUtil.truncate("rpc.info=>{}", buildFeignLogAspectDO(invocation.getMethod(), invocation.getArguments(), result, cost)));
            }

            // 方法调用顺序：apply（初始化值） ——> invoke（获取值，并清除）
            // 3、防止内存泄漏
            FEIGN_HEADER_THREAD_LOCAL.remove();
        }

        return result;
    }

    private FeignLogAspectDO buildFeignLogAspectDO(Method method, Object[] args, Object result, long cost) {
        String classMethod = method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();

        FeignLogAspectDO logDO = new FeignLogAspectDO();
        logDO.setClassMethod(classMethod);
        logDO.setParams(WebUtil.getRequestArgs(args));
        logDO.setResult(result);
        logDO.setHeaders(FEIGN_HEADER_THREAD_LOCAL.get());
        logDO.setCost(cost);
        return logDO;
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