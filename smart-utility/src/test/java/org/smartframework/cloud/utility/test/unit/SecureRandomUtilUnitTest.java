package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.SecureRandomUtil;

import java.security.NoSuchAlgorithmException;

class SecureRandomUtilUnitTest {

    @Test
    void testGenerateRandom() throws NoSuchAlgorithmException {
        String random1 = SecureRandomUtil.generateRandom(true, 10);
        String random2 = SecureRandomUtil.generateRandom(false, 10);
        Assertions.assertThat(random1.length()).isEqualTo(10);
        Assertions.assertThat(random2.length()).isEqualTo(10);
    }

    @Test
    void testGenerateRangeRandom() throws NoSuchAlgorithmException {
        int random1 = SecureRandomUtil.generateRangeRandom(10, 100);
        Assertions.assertThat(random1).isBetween(10, 100);
    }

}