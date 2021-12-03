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
package org.smartframework.cloud.common.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.web.constants.ProtostuffConstant;
import org.smartframework.cloud.common.web.enums.ApiUseSideEnum;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet过滤器
 *
 * @author collin
 * @date 2021-10-31
 */
public class ServletFilter extends OncePerRequestFilter implements Ordered {

    private static final String PROTOBUF_CONTENT_TYPE = ProtostuffConstant.PROTOBUF_MEDIA_TYPE.toString();

    private boolean enableRpcProtostuff = true;

    public ServletFilter(boolean enableRpcProtostuff) {
        this.enableRpcProtostuff = enableRpcProtostuff;
    }

    @Override
    public int getOrder() {
        return OrderConstant.HTTP_FITLER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (enableRpcProtostuff) {
            // feign protobuf content-type设置
            processRpcContentType(request, response);
        }

        chain.doFilter(request, response);
    }

    private void processRpcContentType(HttpServletRequest request, HttpServletResponse response) {
        String requestUri = request.getRequestURI();
        if (!requestUri.contains(ApiUseSideEnum.RPC.getPathSegment())) {
            return;
        }

        String defaultContentType = response.getContentType();
        if (StringUtils.isBlank(defaultContentType)) {
            response.setContentType(PROTOBUF_CONTENT_TYPE);
        }
    }

}