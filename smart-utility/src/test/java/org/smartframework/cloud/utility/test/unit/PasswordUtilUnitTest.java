package org.smartframework.cloud.utility.test.unit;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.PasswordUtil;

import java.security.NoSuchAlgorithmException;

@Slf4j
class PasswordUtilUnitTest {

    @Test
    void testSecure() throws NoSuchAlgorithmException {
        String result = PasswordUtil.secure("123456", PasswordUtil.generateRandomSalt());
        log.info("result={}", result);
        Assertions.assertThat(result).isNotBlank();
    }

    @Test
    void testGenerateRandomSalt() throws NoSuchAlgorithmException {
        String randomSalt = PasswordUtil.generateRandomSalt();
        Assertions.assertThat(randomSalt.length()).isEqualTo(16);
    }

}