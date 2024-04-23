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
package io.github.smart.cloud.starter.locale.aspect;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.starter.configure.constants.OrderConstant;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
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
            Response<?> response = (Response<?>) result;
            if (response == null) {
                return result;
            }
            String msg = StringUtils.isNotBlank(response.getMessage()) ? response.getMessage() : response.getCode();
            Locale locale = LocaleContextHolder.getLocale();
            String localMessage = messageSource.getMessage(msg, null, null, locale);
            if (StringUtils.isNotBlank(localMessage)) {
                response.setMessage(localMessage);
            }
        }
        return result;
    }

}