package org.smartframework.cloud.starter.locale.constant;

import lombok.experimental.UtilityClass;

/**
 * locale常量
 * 
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
public class LocaleConstant {

	/** locale文件后缀 */
	public static final String LOCALE_PROPERTIES_SUFFIX = ".properties";
	/** locale文件目录 */
	public static final String LOCALE_DIR = "i18n/";
	/** locale文件路径 */
	public static final String LOCALE_PATTERN = "classpath*:/" + LOCALE_DIR + "*Messages.properties";

}