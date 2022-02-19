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
package io.github.smart.cloud.api.core.user.context.filter;

import io.github.smart.cloud.api.core.user.context.AbstractUserContext;
import io.github.smart.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 清除用户上下文过滤器
 *
 * @author collin
 * @date 2021-10-31
 */
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
