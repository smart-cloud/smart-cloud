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
package io.github.smart.cloud.starter.web.aspect.interceptor;

import io.github.smart.cloud.common.web.pojo.LogAspectDTO;
import io.github.smart.cloud.common.web.util.WebServletUtil;
import io.github.smart.cloud.constants.LogLevel;
import io.github.smart.cloud.constants.OrderConstant;
import io.github.smart.cloud.starter.configure.properties.ApiLogProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.web.annotation.ApiLog;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.InputStreamSource;
import org.springframework.validation.DataBinder;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

/**
 * 接口日志切面
 *
 * @author collin
 * @date 2019-04-08
 */
@Slf4j
@RequiredArgsConstructor
public class ServletApiLogInterceptor implements MethodInterceptor, Ordered {

    private final SmartProperties smartProperties;
    /**
     * 默认日志最大长度
     */
    private static final int DEFAULT_LOG_MAX_LENGTH = 2048;
    /**
     * 慢日志
     */
    private static final String SLOW_LOG_PATTERN = "api.slow=>{}";
    /**
     * 普通日志
     */
    private static final String LOG_PATTERN = "api.log=>{}";
    /**
     * 错误日志
     */
    private static final String ERROR_LOG_PATTERN = "api.error=>{}";

    @Override
    public int getOrder() {
        return OrderConstant.API_LOG;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return invocation.proceed();
        }

        ApiLogProperties apiLogProperties = smartProperties.getApiLog();
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            long cost = System.currentTimeMillis() - startTime;
            log.error(ERROR_LOG_PATTERN, truncate(buildLogAspectDO(invocation.getArguments(), result, cost), apiLogProperties.getLogMaxLength()), e);
            throw e;
        }

        try {
            if (log.isWarnEnabled()) {
                long cost = System.currentTimeMillis() - startTime;
                if (cost >= apiLogProperties.getSlowApiMinCost()) {
                    log.warn(SLOW_LOG_PATTERN, truncate(buildLogAspectDO(invocation.getArguments(), result, cost), apiLogProperties.getLogMaxLength()));
                } else {
                    Set<String> ignoreUrls = apiLogProperties.getIgnoreUrls();
                    if (ignoreUrls == null || !ignoreUrls.contains(WebServletUtil.getHttpServletRequest().getRequestURI())) {
                        ApiLog apiLog = invocation.getMethod().getAnnotation(ApiLog.class);
                        LogLevel logLevel = (apiLog == null) ? LogLevel.INFO : apiLog.level();
                        if (LogLevel.DEBUG == logLevel && log.isDebugEnabled()) {
                            log.debug(LOG_PATTERN, truncate(buildLogAspectDO(invocation.getArguments(), result, cost), apiLogProperties.getLogMaxLength()));
                        } else if (LogLevel.INFO == logLevel && log.isInfoEnabled()) {
                            log.info(LOG_PATTERN, truncate(buildLogAspectDO(invocation.getArguments(), result, cost), apiLogProperties.getLogMaxLength()));
                        } else if (LogLevel.WARN == logLevel && log.isWarnEnabled()) {
                            log.warn(LOG_PATTERN, truncate(buildLogAspectDO(invocation.getArguments(), result, cost), apiLogProperties.getLogMaxLength()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("print api log error", e);
        }

        return result;
    }

    /**
     * 日志超长截取
     *
     * @param logAspectDTO
     * @param logMaxLength
     * @return
     */
    private String truncate(LogAspectDTO logAspectDTO, Integer logMaxLength) {
        logMaxLength = logMaxLength == null ? DEFAULT_LOG_MAX_LENGTH : logMaxLength;
        String content = JacksonUtil.toJson(logAspectDTO);
        return StringUtils.truncate(content, logMaxLength);
    }

    private LogAspectDTO buildLogAspectDO(Object[] args, Object result, long cost) {
        HttpServletRequest request = WebServletUtil.getHttpServletRequest();
        return LogAspectDTO.builder()
                .url(request.getRequestURI())
                .method(request.getMethod())
                .head(getHeaders(request))
                .args(getRequestArgs(args))
                .cost(cost)
                .result(result)
                .build();
    }

    /**
     * 获取http header部分数据
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Enumeration<String> enumerations = request.getHeaderNames();
        if (enumerations == null) {
            return Collections.emptyMap();
        }

        Map<String, String> headers = new HashMap<>(8);
        while (enumerations.hasMoreElements()) {
            String name = enumerations.nextElement();
            headers.put(name, request.getHeader(name));
        }

        return headers;
    }


    /**
     * 获取有效的请求参数（过滤掉不能序列化的）
     *
     * @param args
     * @return
     */
    private static Object getRequestArgs(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        return Stream.of(args).filter(arg -> !needFilter(arg)).toArray();
    }

    /**
     * 是否需要过滤
     *
     * @param object
     * @return
     */
    private static boolean needFilter(Object object) {
        return object instanceof ServletRequest || object instanceof ServletResponse || object instanceof DataBinder || object instanceof InputStreamSource;
    }

}