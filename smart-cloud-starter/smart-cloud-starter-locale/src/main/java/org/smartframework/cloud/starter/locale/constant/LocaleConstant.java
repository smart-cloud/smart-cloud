package org.smartframework.cloud.starter.locale.constant;

/**
 * locale常量
 *
 * @author liyulin
 * @date 2019-07-15
 */
public interface LocaleConstant {

    /**
     * locale文件后缀
     */
    String LOCALE_PROPERTIES_SUFFIX = ".properties";
    /**
     * locale文件目录
     */
    String LOCALE_DIR = "i18n/";
    /**
     * locale文件路径
     */
    String LOCALE_PATTERN = "classpath*:/" + LOCALE_DIR + "*messages.properties";

}