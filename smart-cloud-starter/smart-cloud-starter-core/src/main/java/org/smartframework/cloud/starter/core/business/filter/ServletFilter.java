package org.smartframework.cloud.starter.core.business.filter;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.business.enums.ApiUseSideEnum;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServletFilter implements Filter {

    private static final String PROTOBUF_CONTENT_TYPE = "application/x-protobuf";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        // feign protobuf content-type设置
        processRpcContentType(servletRequest, servletResponse);

        chain.doFilter(servletRequest, servletResponse);
    }

    private void processRpcContentType(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if (!requestURI.contains(ApiUseSideEnum.RPC.getPathSegment())) {
            return;
        }

        String defaultContentType = servletResponse.getContentType();
        if (StringUtils.isBlank(defaultContentType)) {
            servletResponse.setContentType(PROTOBUF_CONTENT_TYPE);
            servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        }
    }

}