package org.smartframework.cloud.code.generate.util;

import java.util.ResourceBundle;

import org.smartframework.cloud.code.generate.config.Config;

public class PropertiesUtil {

	/**
	 * 读取properties文件
	 * 
	 * @return
	 */
	public static ResourceBundle getPropertiesBundle() {
		ResourceBundle configResource = ResourceBundle.getBundle(Config.CONFIG_NAME);
		String configPath = configResource.getString(Config.PROPERTIES_KEY);
		return ResourceBundle.getBundle(Config.CONFIG_PATH + configPath);
	}

}