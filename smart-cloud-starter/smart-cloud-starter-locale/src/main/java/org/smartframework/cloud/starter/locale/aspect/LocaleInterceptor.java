package org.smartframework.cloud.starter.locale.aspect;

import java.util.Locale;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.common.pojo.vo.RespVO;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;

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
		if (result instanceof RespVO) {
			RespVO<?> resp = (RespVO<?>) result;
			RespHeadVO respHeadVO = resp.getHead();
			String message = respHeadVO.getMessage();
			if (StringUtils.isNotBlank(message)) {
				Locale locale = LocaleContextHolder.getLocale();
				String localMessage = messageSource.getMessage(message, null, null, locale);
				if (StringUtils.isNotBlank(localMessage)) {
					respHeadVO.setMessage(localMessage);
				}
			}
		}
		return result;
	}

}