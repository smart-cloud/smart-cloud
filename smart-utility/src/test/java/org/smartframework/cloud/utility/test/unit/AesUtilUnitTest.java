package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.security.AesUtil;

class AesUtilUnitTest {

	@Test
	void testEncryptAndDecrypt() {
		String password = "123456";
		String plainText = "hello world!";
		String encryptText = AesUtil.encrypt(plainText, password);
		String decryptText = AesUtil.decrypt(encryptText, password);
		Assertions.assertThat(plainText).isEqualTo(decryptText);
	}
	
}