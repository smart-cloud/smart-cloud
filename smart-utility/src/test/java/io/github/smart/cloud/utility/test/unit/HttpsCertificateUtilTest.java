package io.github.smart.cloud.utility.test.unit;

import io.github.smart.cloud.utility.HttpsCertificateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HttpsCertificateUtilTest {

    @Test
    void testEncryptorAndDecrypt() throws IOException {
        List<Date> validTimes = HttpsCertificateUtil.getValidTimes("www.baidu.com", null);
        Assertions.assertThat(validTimes).isNotEmpty();
        Assertions.assertThat(validTimes.get(0)).isAfter(new Date());
    }

}
