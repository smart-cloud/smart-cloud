package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.JasyptUtil;

import junit.framework.TestCase;

public class JasyptUtilUnitTest extends TestCase {

	public void testEncryptorAndDecrypt() {
		String salt = "test";
		String message = "123456";
		String encryptor = JasyptUtil.encryptor(salt, message);
		String decrypt = JasyptUtil.decrypt(salt, encryptor);
		Assertions.assertThat(decrypt).isEqualTo(message);
	}

}