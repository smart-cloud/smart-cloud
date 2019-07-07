package org.smartframework.cloud.basic.module.gateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smartframework.cloud.basic.module.gateway.xss.XssHttpServletRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * XSS 过滤
 * 
 * @author liyulin
 * @date 2019年6月19日 下午8:17:17
 */
@Component
public class XssSecurityFilter extends OncePerRequestFilter {

	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be just invoked once
	 * per request within a single request thread. See
	 * {@link #shouldNotFilterAsyncDispatch()} for details.
	 * <p>
	 * Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);
		filterChain.doFilter(xssRequest, response);
	}
	
}