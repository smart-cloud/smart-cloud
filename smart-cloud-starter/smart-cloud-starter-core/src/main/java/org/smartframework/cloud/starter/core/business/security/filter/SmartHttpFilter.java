package org.smartframework.cloud.starter.core.business.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.business.SmartReqContext;
import org.smartframework.cloud.starter.core.business.security.enums.ApiUseSideEnum;
import org.smartframework.cloud.starter.core.business.security.util.ReqHttpHeadersUtil;

public class SmartHttpFilter implements Filter {

	private static final String PROTOBUF_CONTENT_TYPE = "application/x-protobuf";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		// feign protobuf content-type设置
		processRpcContentType(servletRequest, servletResponse);

		try {
			// local参数设置
			SmartReqContext.setReqHttpHeadersBO(ReqHttpHeadersUtil.getReqHttpHeadersBO());
			
			chain.doFilter(servletRequest, servletResponse);
		} finally {
			SmartReqContext.removeReqHttpHeadersBO();
		}
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