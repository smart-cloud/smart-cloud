package org.smartframework.cloud.utility.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.utility.spring.I18nUtil;
import org.smartframework.cloud.utility.test.integration.prepare.TestApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
class I18nUtilTest {

    @Test
    void testGetMessage() {
        Assertions.assertThat(I18nUtil.getMessage("200")).isEqualTo("success");
        Assertions.assertThat(I18nUtil.getMessage("300", new String[]{"300"})).isEqualTo("300");
    }

}