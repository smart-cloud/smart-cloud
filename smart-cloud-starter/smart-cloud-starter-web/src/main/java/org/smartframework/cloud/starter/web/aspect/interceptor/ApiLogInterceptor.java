package org.smartframework.cloud.starter.web.aspect.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.common.pojo.vo.RespVO;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.core.business.util.WebServletUtil;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.starter.web.aspect.pojo.LogAspectDO;
import org.smartframework.cloud.starter.web.exception.ExceptionHandlerContext;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.core.Ordered;
import org.springframework.validation.DataBinder;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 接口日志切面
 *
 * @author liyulin
 * @date 2019-04-08
 */
@Slf4j
public class ApiLogInterceptor implements MethodInterceptor, Ordered {

    @Override
    public int getOrder() {
        return OrderConstant.API_LOG;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return invocation.proceed();
        }

        long startTime = System.currentTimeMillis();
        HttpServletRequest request = WebServletUtil.getHttpServletRequest();
        LogAspectDO logAspectDO = LogAspectDO.builder()
                .url(request.getPathInfo())
                .method(request.getMethod())
                .head(getHeaders(request))
                .args(getRequestArgs(invocation.getArguments()))
                .build();
        Object result = null;
        try {
            result = invocation.proceed();
            long endTime = System.currentTimeMillis();
            // 执行结果
            String resultJson = JacksonUtil.toJson(result);
            resultJson = LogUtil.truncate(resultJson);

            logAspectDO.setResult(resultJson);
            logAspectDO.setCost(endTime - startTime);
            log.info(LogUtil.truncate("api.logDO.info=>{}", logAspectDO));
            return result;
        } catch (Exception e) {
            logAspectDO.setCost(System.currentTimeMillis() - startTime);

            log.error(LogUtil.truncate("api.logDO.error=>{}", logAspectDO), e);

            RespHeadVO head = ExceptionHandlerContext.transRespHead(e);
            return new RespVO<>(head);
        }
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
            return null;
        }

        Map<String, String> headers = new HashMap<>();
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
        return object instanceof ServletRequest || object instanceof ServletResponse || object instanceof DataBinder;
    }

    /**
     * 获取有效的参数（如果是request对象，则优先从ParameterMap里取）
     *
     * @param args
     * @return
     */
    private static Object getValidArgs(Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return args;
        }

        if (args.length == 1 && args[0] instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) args[0];
            return request.getParameterMap();
        }

        return args;
    }

}