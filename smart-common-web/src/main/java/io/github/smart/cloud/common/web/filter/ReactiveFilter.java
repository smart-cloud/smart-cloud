/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.common.web.filter;

import io.github.smart.cloud.constants.OrderConstant;
import org.springframework.core.Ordered;
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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Reactor Context 是异步链路的主上下文；ThreadLocal 仅在当前同步调用栈中兼容旧 API。
        return Mono.deferContextual(contextView -> {
            ServerWebExchange previous = ReactiveRequestContextHolder.getThreadLocalServerWebExchange();
            ReactiveRequestContextHolder.setServerWebExchange(
                    ReactiveRequestContextHolder.getServerWebExchange(contextView));
            try {
                return chain.filter(exchange)
                        .doFinally(signal -> ReactiveRequestContextHolder.restoreServerWebExchange(previous));
            } catch (RuntimeException | Error e) {
                ReactiveRequestContextHolder.restoreServerWebExchange(previous);
                throw e;
            }
        }).contextWrite(context -> context.put(
                ReactiveRequestContextHolder.SERVER_WEB_EXCHANGE_CONTEXT_KEY, exchange));
    }

}