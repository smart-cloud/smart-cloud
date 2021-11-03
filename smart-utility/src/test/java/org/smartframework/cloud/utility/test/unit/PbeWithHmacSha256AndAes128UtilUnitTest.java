package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.security.PbeWithHmacSha256AndAes128Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

class PbeWithHmacSha256AndAes128UtilUnitTest {

    @Test
    void testPBEWithMD5AndDES() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String password = "123456";
        String word = "test";
        String encryptWord = PbeWithHmacSha256AndAes128Util.encrypt(password, word);
        String decryptWord = PbeWithHmacSha256AndAes128Util.decrypt(password, encryptWord);
        Assertions.assertThat(decryptWord).isEqualTo(word);
    }

}