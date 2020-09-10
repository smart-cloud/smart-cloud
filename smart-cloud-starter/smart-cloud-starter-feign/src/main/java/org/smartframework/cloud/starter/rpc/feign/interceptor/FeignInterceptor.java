package org.smartframework.cloud.starter.rpc.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.smartframework.cloud.starter.core.business.util.WebReactiveUtil;
import org.smartframework.cloud.starter.core.business.util.WebUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.rpc.feign.pojo.FeignLogAspectDO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * feign切面
 *
 * @author liyulin
 * @date 2019-04-21
 */
@Slf4j
public class FeignInterceptor implements MethodInterceptor, RequestInterceptor {

    private static final ThreadLocal<Map<String, Collection<String>>> FEIGN_HEADER_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();

        FeignLogAspectDO logDO = new FeignLogAspectDO();
        Method method = invocation.getMethod();
        String classMethod = method.getDeclaringClass().getSimpleName() + SymbolConstant.DOT + method.getName();
        logDO.setClassMethod(classMethod);

        logDO.setReqParams(WebUtil.getRequestArgs(invocation.getArguments()));
        logDO.setReqHeaders(FEIGN_HEADER_THREAD_LOCAL.get());

        // 2、rpc
        Object result = invocation.proceed();

        logDO.setCost(System.currentTimeMillis() - startTime);
        logDO.setRespData(result);

        // 3、打印日志
        log.info(LogUtil.truncate("rpc.logDO=>{}", logDO));

        // 方法调用顺序：apply（初始化值） ——> invoke（获取值，并清除）
        // 防止内存泄漏
        FEIGN_HEADER_THREAD_LOCAL.remove();

        return result;
    }

    @Override
    public void apply(RequestTemplate template) {
        // 填充header信息
        if (WebUtil.isWebFlux()) {
            fillReactiveHeader(template);
        } else {
            fillServletHeader(template);
        }

        FEIGN_HEADER_THREAD_LOCAL.set(template.headers());
    }

    private void fillReactiveHeader(RequestTemplate template) {
        ServerHttpRequest serverHttpRequest = WebReactiveUtil.getServerHttpRequest();
        HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
        if (httpHeaders == null) {
            return;
        }

        httpHeaders.forEach((k, v) -> {
            template.header(k, v);
        });
    }

    private void fillServletHeader(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            template.header(name, request.getHeader(name));
        }
    }

}