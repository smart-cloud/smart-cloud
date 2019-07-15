package org.smartframework.cloud.starter.common.test.unit;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.starter.common.SuiteTest;
import org.smartframework.cloud.starter.common.business.util.ReflectionUtil;
import org.smartframework.cloud.starter.common.constants.PackageConfig;

import junit.framework.TestCase;

public class ReflectionUtilUnitTest extends TestCase {
	
	public void testGetSubTypesOf() {
		PackageConfig.setBasePackages(new String[]{SuiteTest.class.getPackage().getName()});
		 Set<Class<? extends TestCase>> set = ReflectionUtil.getSubTypesOf(TestCase.class);
		 Assertions.assertThat(set).isNotEmpty();
	}
	
}