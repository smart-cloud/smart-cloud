package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.ObjectUtil;

import junit.framework.TestCase;

public class ObjectUtilUnitTest extends TestCase {

	public void testIsNull() {
		Assertions.assertThat(ObjectUtil.isNull(null)).isTrue();
		Assertions.assertThat(ObjectUtil.isNull("")).isFalse();
	}

	public void testIsNotNull() {
		Assertions.assertThat(ObjectUtil.isNotNull(null)).isFalse();
		Assertions.assertThat(ObjectUtil.isNotNull("")).isTrue();
	}

	public void testIsAllNull() {
		Assertions.assertThat(ObjectUtil.isAllNull(null, null)).isTrue();
		Assertions.assertThat(ObjectUtil.isAllNull(null, "")).isFalse();
		Assertions.assertThat(ObjectUtil.isAllNull("", "")).isFalse();
	}

	public void testIsAllNotNull() {
		Assertions.assertThat(ObjectUtil.isAllNotNull(null, null)).isFalse();
		Assertions.assertThat(ObjectUtil.isAllNotNull(null, "")).isFalse();
		Assertions.assertThat(ObjectUtil.isAllNotNull("", "")).isTrue();
	}

	public void testEquals() {
		Assertions.assertThat(ObjectUtil.equals("123", "123")).isTrue();
		Assertions.assertThat(ObjectUtil.equals("1234", "123")).isFalse();
	}

}