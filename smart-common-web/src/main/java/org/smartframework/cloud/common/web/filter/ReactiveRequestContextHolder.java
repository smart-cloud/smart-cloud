package org.smartframework.cloud.common.web.filter;

import lombok.experimental.UtilityClass;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * reactive上下文
 *
 * @author collin
 * @date 2021-11-11
 */
@UtilityClass
public class ReactiveRequestContextHolder {

    private static final ThreadLocal<ServerWebExchange> SERVER_WEB_EXCHANGE_HOLDER = new NamedThreadLocal<>(
            "ServerWebExchange context");

    public static ServerWebExchange getServerWebExchange() {
        return SERVER_WEB_EXCHANGE_HOLDER.get();
    }

    public static void setServerWebExchange(ServerWebExchange exchange) {
        SERVER_WEB_EXCHANGE_HOLDER.set(exchange);
    }

    public static void removeServerWebExchange() {
        SERVER_WEB_EXCHANGE_HOLDER.remove();
    }

    public static HttpHeaders getHttpHeaders() {
        ServerHttpRequest serverHttpRequest = getServerHttpRequest();
        if (serverHttpRequest == null) {
            return null;
        }
        return serverHttpRequest.getHeaders();
    }

    public static ServerHttpRequest getServerHttpRequest() {
        ServerWebExchange exchange = SERVER_WEB_EXCHANGE_HOLDER.get();
        if (exchange == null) {
            return null;
        }
        return exchange.getRequest();
    }

    public static ServerHttpResponse getServerHttpResponse() {
        ServerWebExchange exchange = SERVER_WEB_EXCHANGE_HOLDER.get();
        if (exchange == null) {
            return null;
        }
        return exchange.getResponse();
    }

}