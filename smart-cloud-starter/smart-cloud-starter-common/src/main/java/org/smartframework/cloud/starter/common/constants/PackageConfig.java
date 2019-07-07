package org.smartframework.cloud.starter.common.constants;

import org.smartframework.cloud.utility.ArrayUtil;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PackageConfig {

	/** 基础包 */
	@Setter
	private static String[] basePackages = null;
	
	public static String[] getBasePackages() {
		Assert.isTrue(ArrayUtil.isNotEmpty(PackageConfig.basePackages), "basePackages未配置！！！");
		
		return basePackages;
	}

}