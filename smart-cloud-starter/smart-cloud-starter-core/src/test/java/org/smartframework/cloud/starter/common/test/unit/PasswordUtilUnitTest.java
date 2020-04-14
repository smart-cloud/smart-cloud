package org.smartframework.cloud.starter.common.test.unit;

import java.security.NoSuchAlgorithmException;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.starter.core.business.util.PasswordUtil;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordUtilUnitTest extends TestCase {

	public void testSecure() throws NoSuchAlgorithmException {
		String result = PasswordUtil.secure("123456", PasswordUtil.generateRandomSalt());
		log.info("result={}", result);
		Assertions.assertThat(result).isNotBlank();
	}

	public void testGenerateRandomSalt() throws NoSuchAlgorithmException {
		String randomSalt = PasswordUtil.generateRandomSalt();
		Assertions.assertThat(randomSalt.length()).isEqualTo(16);
	}
	
}