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
package io.github.smart.cloud.starter.rpc.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.smart.cloud.common.web.filter.ReactiveRequestContextHolder;
import io.github.smart.cloud.common.web.util.WebUtil;
import io.github.smart.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign http header参数传递
 *
 * @author collin
 * @date 2019-04-21
 */
public class FeignHttpHeaderParameterInterceptor implements RequestInterceptor, Ordered {

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
        serverHttpRequest.getHeaders().forEach(template::header);
    }

    private void fillServletHeader(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
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