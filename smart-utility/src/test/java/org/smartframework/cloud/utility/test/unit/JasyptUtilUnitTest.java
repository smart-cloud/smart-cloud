package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.JasyptUtil;

public class JasyptUtilUnitTest {

    @Test
    public void testEncryptorAndDecrypt() {
        String salt = "test";
        String message = "123456";
        String encryptor = JasyptUtil.encryptor(salt, message);
        String decrypt = JasyptUtil.decrypt(salt, encryptor);
        Assertions.assertThat(decrypt).isEqualTo(message);
    }

}