package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.security.PBEWithMD5AndDESUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class PBEWithMD5AndDESUtilUnitTest {

    @Test
    void testPBEWithMD5AndDES() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String password = "123456";
        String word = "test";
        String encryptWord = PBEWithMD5AndDESUtil.encrypt(password, word);
        String decryptWord = PBEWithMD5AndDESUtil.decrypt(password, encryptWord);
        Assertions.assertThat(decryptWord).isEqualTo(word);
    }

}