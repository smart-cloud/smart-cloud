package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.security.Md5Util;

import java.io.IOException;

class Md5UtilUnitTest {

    @Test
    void testGenerateFileMd5() throws IOException {
        String path = Md5UtilUnitTest.class.getResource("").getFile() + Md5UtilUnitTest.class.getSimpleName()
                + ".class";

        String md5 = Md5Util.generateFileMd5(path);
        Assertions.assertThat(md5).isNotBlank();
    }

}