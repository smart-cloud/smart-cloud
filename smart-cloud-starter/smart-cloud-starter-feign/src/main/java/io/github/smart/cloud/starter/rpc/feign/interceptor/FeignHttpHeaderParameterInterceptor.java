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
import io.github.smart.cloud.common.web.constants.SmartHttpHeaders;
import io.github.smart.cloud.common.web.filter.ReactiveRequestContextHolder;
import io.github.smart.cloud.common.web.util.WebUtil;
import io.github.smart.cloud.starter.configure.constants.OrderConstant;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * feign http header参数传递
 *
 * @author collin
 * @date 2019-04-21
 */
@RequiredArgsConstructor
public class FeignHttpHeaderParameterInterceptor implements RequestInterceptor, Ordered {

    private final SmartProperties smartProperties;

    /**
     * 默认的需要传递的请求参数名称
     */
    public static final Set<String> DEFAULT_TRANSFER_HEADER_NAMES = new HashSet<>();

    static {
        DEFAULT_TRANSFER_HEADER_NAMES.add(SmartHttpHeaders.HEADER_USER);
    }

    @Override
    public void apply(RequestTemplate template) {
        Set<String> transferHeaderNames = smartProperties.getFeign().getTransferHeaderNames();
        if (CollectionUtils.isEmpty(transferHeaderNames)) {
            transferHeaderNames = DEFAULT_TRANSFER_HEADER_NAMES;
        }

        // 填充header信息
        if (WebUtil.isWebFlux()) {
            fillReactiveHeader(template, transferHeaderNames);
        } else {
            fillServletHeader(template, transferHeaderNames);
        }
    }

    @Override
    public int getOrder() {
        return OrderConstant.FEIGN_HEADER;
    }

    private void fillReactiveHeader(RequestTemplate template, Set<String> transferHeaderNames) {
        ServerHttpRequest serverHttpRequest = ReactiveRequestContextHolder.getServerHttpRequest();
        if (serverHttpRequest == null) {
            return;
        }

        HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
        if (MapUtils.isEmpty(httpHeaders)) {
            return;
        }

        for (String transferHeaderName : transferHeaderNames) {
            if (httpHeaders.containsKey(transferHeaderName)) {
                template.header(transferHeaderName, httpHeaders.get(transferHeaderName));
            }
        }
    }

    private void fillServletHeader(RequestTemplate template, Set<String> transferHeaderNames) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        for (String transferHeaderName : transferHeaderNames) {
            String headerValue = request.getHeader(transferHeaderName);
            if (headerValue != null) {
                template.header(transferHeaderName, headerValue);
            }
        }
    }

}