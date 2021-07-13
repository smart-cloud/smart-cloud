package org.smartframework.cloud.api.core.user.context.filter;

import org.smartframework.cloud.api.core.user.context.AbstractUserContext;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CleanUserContextReactiveFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return OrderConstant.CLEAN_USER_CONTEXT_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(signal -> AbstractUserContext.remove());
    }

}
