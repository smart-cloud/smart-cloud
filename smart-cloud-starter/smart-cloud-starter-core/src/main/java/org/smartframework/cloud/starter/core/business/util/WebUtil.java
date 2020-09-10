package org.smartframework.cloud.starter.core.business.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.business.filter.ReactiveRequestContextHolder;
import org.springframework.util.ClassUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * web相关工具类
 *
 * @author liyulin
 * @date 2019-04-03
 */
@UtilityClass
@Slf4j
public class WebUtil {

	/** http head user-agent */
	private static final String USER_AGENT = "User-Agent";
	private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";
	private static final boolean WEBFLUX_PRESENT = ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null);

	public static final boolean isWebFlux() {
		return WEBFLUX_PRESENT;
	}

	/**
	 * 获取本机ip地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIP() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * 获取请求的ip地址
	 *
	 * @return ip地址
	 */
	public static String getRealIP() {
		if (isWebFlux()) {
			return WebReactiveUtil.getRealIP();
		}

		return WebServletUtil.getRealIP();
	}

	public static String getMappingPath() {
		if (isWebFlux()) {
			return WebReactiveUtil.getServerHttpRequest().getPath().contextPath().value();
		}

		return WebServletUtil.getHttpServletRequest().getServletPath();
	}

	public static String getHttpMethod() {
		if (isWebFlux()) {
			return WebReactiveUtil.getServerHttpRequest().getMethod().name();
		}

		return WebServletUtil.getHttpServletRequest().getMethod();
	}

	public static String getUserAgent() {
		if (isWebFlux()) {
			return ReactiveRequestContextHolder.getHttpHeaders().getFirst(USER_AGENT);
		}

		return WebServletUtil.getHttpServletRequest().getHeader(USER_AGENT);
	}

	/**
	 * ip地址是否可以ping通
	 *
	 * @param ipAddress ip地址
	 * @param timeout   超时，单位毫秒
	 * @return true | false
	 */
	public static boolean ipCanPing(String ipAddress, int timeout) {
		if (StringUtils.isEmpty(ipAddress)) {
			return false;
		}

		boolean canPing = false;
		try {
			canPing = InetAddress.getByName(ipAddress).isReachable(timeout);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return canPing;
	}

	/**
	 * 链接是否可以被请求
	 *
	 * @param url     地址
	 * @param timeout 超时，单位毫秒
	 * @return true | false
	 */
	public static boolean addrCanConnect(String url, int timeout) {
		if (StringUtils.isEmpty(url)) {
			return false;
		}

		boolean canConnect = false;
		try {
			URL urlConnection = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
			connection.setUseCaches(false);
			connection.setConnectTimeout(timeout);
			int state = connection.getResponseCode();
			canConnect = (state == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return canConnect;
	}

	/**
	 * 获取有效的请求参数（过滤掉不能序列化的）
	 * 
	 * @param args
	 * @return
	 */
	public static Object getRequestArgs(Object[] args) {
		if (isWebFlux()) {
			return WebReactiveUtil.getRequestArgs(args);
		}

		return WebServletUtil.getRequestArgs(args);
	}

}