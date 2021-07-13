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
        String requestURI = request.getRequestURI();
        if (!requestURI.contains(ApiUseSideEnum.RPC.getPathSegment())) {
            return;
        }

        String defaultContentType = response.getContentType();
        if (StringUtils.isBlank(defaultContentType)) {
            response.setContentType(PROTOBUF_CONTENT_TYPE);
        }
    }

}