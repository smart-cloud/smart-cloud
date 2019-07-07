package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.security.AesUtil;

import junit.framework.TestCase;

public class AesUtilUnitTest extends TestCase{

	public void testEncryptAndDecrypt() {
		String password = "123456";
		String plainText = "hello world!";
		String encryptText = AesUtil.encrypt(plainText, password);
		String decryptText = AesUtil.decrypt(encryptText, password);
		Assertions.assertThat(plainText).isEqualTo(decryptText);
	}
	
}