package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.ArrayUtil;

import junit.framework.TestCase;

public class ArrayUtilUnitTest extends TestCase {

	public void testIsEmpty() {
		Assertions.assertThat(ArrayUtil.isEmpty(null)).isTrue();
		Assertions.assertThat(ArrayUtil.isEmpty(new String[0])).isTrue();
		Assertions.assertThat(ArrayUtil.isEmpty(new String[1])).isFalse();
	}
	
	public void testIsNotEmpty() {
		Assertions.assertThat(ArrayUtil.isNotEmpty(null)).isFalse();
		Assertions.assertThat(ArrayUtil.isNotEmpty(new String[0])).isFalse();
		Assertions.assertThat(ArrayUtil.isNotEmpty(new String[1])).isTrue();
	}
	
}