package org.smartframework.cloud.utility.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author liyulin
 * @desc 国际化工具类
 * @date 2020/04/26
 */
public class I18nUtil implements ApplicationListener<ContextRefreshedEvent> {

    private static MessageSource messageSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        messageSource = event.getApplicationContext().getBean(MessageSource.class);
    }

    public static String getMessage(String message) {
        return getMessage(message, null);
    }

    public static String getMessage(String message, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, args, null, locale);
    }

}