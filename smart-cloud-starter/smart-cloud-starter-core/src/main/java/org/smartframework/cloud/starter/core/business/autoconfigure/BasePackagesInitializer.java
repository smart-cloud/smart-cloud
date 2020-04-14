package org.smartframework.cloud.starter.core.business.autoconfigure;

import org.smartframework.cloud.starter.core.constants.PackageConfig;
import org.smartframework.cloud.starter.core.support.annotation.SmartSpringCloudApplication;

/**
 * @desc BasePackages初始化
 * @author liyulin
 * @date 2020/02/23
 */
public class BasePackagesInitializer {

	/**
	 * 设置{@link ComponentScan}的{@code basePackages}
	 * 
	 * @param smartSpringCloudApplication
	 */
	public void init(SmartSpringCloudApplication smartSpringCloudApplication) {
		String[] componentBasePackages = smartSpringCloudApplication.componentBasePackages();
		PackageConfig.setBasePackages(componentBasePackages);
	}

}