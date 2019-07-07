package org.smartframework.cloud.utility.test.unit;

import java.security.NoSuchAlgorithmException;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.SecureRandomUtil;

import junit.framework.TestCase;

public class SecureRandomUtilUnitTest extends TestCase {

	public void testGenerateRandom() throws NoSuchAlgorithmException {
		String random1 = SecureRandomUtil.generateRandom(true, 10);
		String random2 = SecureRandomUtil.generateRandom(false, 10);
		Assertions.assertThat(random1.length()).isEqualTo(10);
		Assertions.assertThat(random2.length()).isEqualTo(10);
	}

	public void testGenerateRangeRandom() throws NoSuchAlgorithmException {
		int random1 = SecureRandomUtil.generateRangeRandom(10, 100);
		Assertions.assertThat(random1).isBetween(10, 100);
	}
	
}