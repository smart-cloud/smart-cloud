package org.smartframework.cloud.starter.rpc.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.smartframework.cloud.common.web.filter.ReactiveRequestContextHolder;
import org.smartframework.cloud.common.web.util.WebUtil;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign上下文参数传递
 *
 * @author liyulin
 * @date 2019-04-21
 */
public class FeignSessionInterceptor implements RequestInterceptor, Ordered {

    @Override
    public void apply(RequestTemplate template) {
        // 填充header信息
        if (WebUtil.isWebFlux()) {
            fillReactiveHeader(template);
        } else {
            fillServletHeader(template);
        }
    }

    @Override
    public int getOrder() {
        return OrderConstant.FEIGN_SESSION;
    }

    private void fillReactiveHeader(RequestTemplate template) {
        ServerHttpRequest serverHttpRequest = ReactiveRequestContextHolder.getServerHttpRequest();
        if (serverHttpRequest == null) {
            return;
        }
        HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
        if (httpHeaders == null) {
            return;
        }
        httpHeaders.forEach((name, value) -> {
            if (needCopy(name)) {
                template.header(name, value);
            }
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
            if (needCopy(name)) {
                template.header(name, request.getHeader(name));
            }
        }
    }

    private boolean needCopy(String name) {
        // 过滤掉“Content-Length”，否则rpc用“protobuf”序列化会报错
        return !HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name);
    }

}