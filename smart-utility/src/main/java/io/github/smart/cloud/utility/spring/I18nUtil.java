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
package io.github.smart.cloud.utility.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

import java.util.Locale;

/**
 * @author collin
 * @desc 国际化工具类
 * @date 2020/04/26
 */
public class I18nUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static MessageSource messageSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        I18nUtil.messageSource = event.getApplicationContext().getBean(MessageSource.class);
    }

    public static String getMessage(String message) {
        return getMessage(message, null);
    }

    public static String getMessage(String message, Object[] args) {
        Assert.notNull(messageSource, "MessageSource is not inited!");
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, args, null, locale);
    }

}