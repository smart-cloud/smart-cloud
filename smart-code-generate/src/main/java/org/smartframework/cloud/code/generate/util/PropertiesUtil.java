package org.smartframework.cloud.code.generate.util;

import java.util.ResourceBundle;

public class PropertiesUtil {

	/**
	 * 读取properties文件
	 * 
	 * @param configName 配置文件路径（注：名为“mall_product.properties”，则值为“config/mall_product”）
	 * @return
	 */
	public static ResourceBundle getBundle(String configName) {
		return ResourceBundle.getBundle(configName);
	}

}