package org.smartframework.cloud.starter.core.business.filter;

import lombok.experimental.UtilityClass;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

@UtilityClass
public class ReactiveRequestContextHolder {

	private static final ThreadLocal<ServerWebExchange> serverWebExchangeHolder = new NamedThreadLocal<>(
			"ServerWebExchange context");

	public static ServerWebExchange getServerWebExchange() {
		return serverWebExchangeHolder.get();
	}

	public static void setServerWebExchange(ServerWebExchange exchange) {
		serverWebExchangeHolder.set(exchange);
	}

	public static void removeServerWebExchange() {
		serverWebExchangeHolder.remove();
	}

	public static HttpHeaders getHttpHeaders() {
		ServerHttpRequest serverHttpRequest = getServerHttpRequest();
		if (serverHttpRequest == null) {
			return null;
		}
		return serverHttpRequest.getHeaders();
	}

	public static ServerHttpRequest getServerHttpRequest() {
		ServerWebExchange exchange = serverWebExchangeHolder.get();
		if (exchange == null) {
			return null;
		}
		return exchange.getRequest();
	}

	public static ServerHttpResponse getServerHttpResponse() {
		ServerWebExchange exchange = serverWebExchangeHolder.get();
		if (exchange == null) {
			return null;
		}
		return exchange.getResponse();
	}

}