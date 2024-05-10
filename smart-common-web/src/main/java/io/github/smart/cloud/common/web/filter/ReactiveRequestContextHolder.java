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
            return HttpHeaders.EMPTY;
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