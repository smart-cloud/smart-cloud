package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.SystemUtil;

import junit.framework.TestCase;

public class SystemUtilUnitTest extends TestCase {

	public void testIsWindows() {
		Assertions.assertThat(SystemUtil.isWindows()).isTrue();
	}

	public void testIsLinux() {
		Assertions.assertThat(SystemUtil.isLinux()).isFalse();
	}

	public void testGetUserDir() {
		Assertions.assertThat(SystemUtil.getUserDir()).isNotBlank();
	}
	
}