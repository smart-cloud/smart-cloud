package org.smartframework.cloud.starter.core.test.unit;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.core.business.util.PasswordUtil;

import java.security.NoSuchAlgorithmException;

@Slf4j
public class PasswordUtilUnitTest {

    @Test
    public void testSecure() throws NoSuchAlgorithmException {
        String result = PasswordUtil.secure("123456", PasswordUtil.generateRandomSalt());
        log.info("result={}", result);
        Assertions.assertThat(result).isNotBlank();
    }

    @Test
    public void testGenerateRandomSalt() throws NoSuchAlgorithmException {
        String randomSalt = PasswordUtil.generateRandomSalt();
        Assertions.assertThat(randomSalt.length()).isEqualTo(16);
    }

}