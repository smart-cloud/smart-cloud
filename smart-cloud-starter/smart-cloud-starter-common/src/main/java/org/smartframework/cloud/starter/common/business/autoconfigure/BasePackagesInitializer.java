package org.smartframework.cloud.starter.common.business.autoconfigure;

import org.smartframework.cloud.starter.common.constants.PackageConfig;
import org.smartframework.cloud.starter.common.support.annotation.SmartSpringCloudApplication;

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