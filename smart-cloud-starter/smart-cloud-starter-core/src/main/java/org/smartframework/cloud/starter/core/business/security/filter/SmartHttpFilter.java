package org.smartframework.cloud.starter.core.business.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.smartframework.cloud.starter.core.business.SmartReqContext;
import org.smartframework.cloud.starter.core.business.security.util.ReqHttpHeadersUtil;

public class SmartHttpFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		SmartReqContext.setReqHttpHeadersBO(ReqHttpHeadersUtil.getReqHttpHeadersBO());
		chain.doFilter(request, response);
		SmartReqContext.removeReqHttpHeadersBO();
	}

}