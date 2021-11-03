package org.smartframework.cloud.starter.locale.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;

import java.util.Locale;

/**
 * 国际化拦截器
 *
 * @author collin
 * @date 2021-11-11
 */
public class LocaleInterceptor implements MethodInterceptor, Ordered {

    private MessageSource messageSource;

    public LocaleInterceptor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public int getOrder() {
        return OrderConstant.LOCALE;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result instanceof Response) {
            Response<?> resp = (Response<?>) result;
            ResponseHead respHeadVO = resp.getHead();
            if (respHeadVO == null) {
                return result;
            }
            String msg = StringUtils.isNotBlank(respHeadVO.getMessage()) ? respHeadVO.getMessage() : respHeadVO.getCode();
            Locale locale = LocaleContextHolder.getLocale();
            String localMessage = messageSource.getMessage(msg, null, null, locale);
            if (StringUtils.isNotBlank(localMessage)) {
                respHeadVO.setMessage(localMessage);
            }
        }
        return result;
    }

}