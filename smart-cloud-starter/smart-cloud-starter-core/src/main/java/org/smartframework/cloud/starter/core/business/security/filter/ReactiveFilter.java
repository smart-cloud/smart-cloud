package org.smartframework.cloud.starter.core.business.security.filter;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.core.business.SmartReqContext;
import org.smartframework.cloud.starter.core.business.security.ReactiveRequestContextHolder;
import org.smartframework.cloud.starter.core.business.security.enums.ApiUseSideEnum;
import org.smartframework.cloud.starter.core.business.security.util.ReqHttpHeadersUtil;
import org.smartframework.cloud.starter.core.constants.ProtostuffConstant;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class ReactiveFilter implements WebFilter, Ordered {

	@Override
	public int getOrder() {
		return OrderConstant.HTTP_FITLER;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		// rpc 接口content-type设置
		processRpcContentType(exchange.getRequest(), exchange.getResponse());
		
		// 缓存ServerWebExchange（request、response）
		ReactiveRequestContextHolder.setServerWebExchange(exchange);

		// local参数设置
		SmartReqContext.setReqHttpHeadersBO(ReqHttpHeadersUtil.getReqHttpHeadersBO());
		return chain.filter(exchange).doFinally(signal -> {
			SmartReqContext.removeReqHttpHeadersBO();
			ReactiveRequestContextHolder.removeServerWebExchange();
		});
	}

	private void processRpcContentType(ServerHttpRequest request, ServerHttpResponse response) {
		String requestURI = request.getURI().getPath();
		if (!requestURI.contains(ApiUseSideEnum.RPC.getPathSegment())) {
			return;
		}
		HttpHeaders httpHeaders = response.getHeaders();
		MediaType mediaType = httpHeaders.getContentType();
		if (mediaType == null || StringUtils.isBlank(mediaType.getSubtype())) {
			httpHeaders.setContentType(ProtostuffConstant.PROTOBUF_MEDIA_TYPE);
		}
	}
	
}