package org.smartframework.cloud.common.web.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.exception.ServerException;
import org.smartframework.cloud.common.web.filter.ReactiveRequestContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.stream.Stream;

/**
 * web相关工具类
 *
 * @author liyulin
 * @date 2019-04-03
 */
@UtilityClass
public class WebReactiveUtil {

    /**
     * 获取请求的ip地址
     *
     * @return ip地址
     */
    public static String getRealIP() {
        HttpHeaders httpHeaders = ReactiveRequestContextHolder.getHttpHeaders();
        if (httpHeaders == null) {
            return null;
        }
        String ip = httpHeaders.getFirst("x-forwarded-for");
        String unknown = "unknown";
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            InetSocketAddress inetSocketAddress = ReactiveRequestContextHolder.getServerHttpRequest().getRemoteAddress();
            if (inetSocketAddress != null) {
                ip = inetSocketAddress.toString();
            }
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("http_client_ip");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("HTTP_X_FORWARDED_FOR");
        }

        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(',') + 1, ip.length()).trim();
        }
        return ip;
    }

    /**
     * 获取有效的请求参数（过滤掉不能序列化的）
     *
     * @param args
     * @return
     */
    public static Object getRequestArgs(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        boolean needFilter = false;
        for (Object arg : args) {
            if (needFilter(arg)) {
                needFilter = true;
                break;
            }
        }

        if (!needFilter) {
            return args.length == 1 ? args[0] : args;
        }

        Object[] tempArgs = Stream.of(args).filter(arg -> !needFilter(arg)).toArray();

        return getValidArgs(tempArgs);
    }

    /**
     * 是否需要过滤
     *
     * @param object
     * @return
     */
    private static boolean needFilter(Object object) {
        return !(object instanceof Serializable);
    }

    /**
     * 获取有效的参数（如果是request对象，则优先从MultiValueMap里取）
     *
     * @param args
     * @return
     */
    private static Object getValidArgs(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        if (args.length == 1 && args[0] instanceof ServerHttpRequest) {
            ServerHttpRequest request = (ServerHttpRequest) args[0];
            return request.getQueryParams();
        }

        return args;
    }

    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    public static ServerHttpRequest getServerHttpRequest() {
        ServerHttpRequest request = ReactiveRequestContextHolder.getServerHttpRequest();
        if (request != null) {
            return request;
        }

        throw new ServerException(CommonReturnCodes.GET_REQUEST_FAIL);
    }


    /**
     * 获取HttpServletResponse
     *
     * @return
     */
    public static ServerHttpResponse getServerHttpResponse() {
        ServerHttpResponse response = ReactiveRequestContextHolder.getServerHttpResponse();
        if (response != null) {
            return response;
        }

        throw new ServerException(CommonReturnCodes.GET_RESPONSE_FAIL);
    }

}