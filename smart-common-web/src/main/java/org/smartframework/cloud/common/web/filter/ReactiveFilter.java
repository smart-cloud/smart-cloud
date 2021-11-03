package org.smartframework.cloud.common.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.web.constants.ProtostuffConstant;
import org.smartframework.cloud.common.web.enums.ApiUseSideEnum;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * reactive过滤器
 *
 * @author collin
 * @date 2021-11-11
 */
public class ReactiveFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return OrderConstant.HTTP_FITLER;
    }

    private boolean enableRpcProtostuff = true;

    public ReactiveFilter(boolean enableRpcProtostuff) {
        this.enableRpcProtostuff = enableRpcProtostuff;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (enableRpcProtostuff) {
            // rpc 接口content-type设置
            processRpcContentType(exchange.getRequest(), exchange.getResponse());
        }

        // 缓存ServerWebExchange（request、response）
        ReactiveRequestContextHolder.setServerWebExchange(exchange);

        // local参数设置
        return chain.filter(exchange).doFinally(signal -> ReactiveRequestContextHolder.removeServerWebExchange());
    }

    private void processRpcContentType(ServerHttpRequest request, ServerHttpResponse response) {
        String requestUri = request.getURI().getPath();
        if (!requestUri.contains(ApiUseSideEnum.RPC.getPathSegment())) {
            return;
        }
        HttpHeaders httpHeaders = response.getHeaders();
        MediaType mediaType = httpHeaders.getContentType();
        if (mediaType == null || StringUtils.isBlank(mediaType.getSubtype())) {
            httpHeaders.setContentType(ProtostuffConstant.PROTOBUF_MEDIA_TYPE);
        }
    }

}