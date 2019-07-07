package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.RandomUtil;

import junit.framework.TestCase;

public class RandomUtilUnitTest extends TestCase {

	public void testGenerateRandom() {
		String random1 = RandomUtil.generateRandom(true, 10);
		String random2 = RandomUtil.generateRandom(false, 10);
		Assertions.assertThat(random1.length()).isEqualTo(10);
		Assertions.assertThat(random2.length()).isEqualTo(10);
	}
	
	public void testGenerateRangeRandom() {
		int random1 = RandomUtil.generateRangeRandom(10, 100);
		Assertions.assertThat(random1).isBetween(10, 100);
	}
	
}