package org.smartframework.cloud.api.core.user.context.filter;

import org.smartframework.cloud.api.core.user.context.AbstractUserContext;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 清除用户上下文过滤器
 *
 * @author collin
 * @date 2021-10-31
 */
public class CleanUserContextServletFilter extends OncePerRequestFilter implements Ordered {

    @Override
    public int getOrder() {
        return OrderConstant.CLEAN_USER_CONTEXT_FILTER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        AbstractUserContext.remove();
    }

}