package org.smartframework.cloud.utility.test.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.utility.spring.SpringContextUtil;
import org.smartframework.cloud.utility.test.integration.prepare.TestApplication;
import org.smartframework.cloud.utility.test.integration.prepare.controller.IntegrationTestController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
class SpringContextUtilIntegrationTest {

    @Test
    void testGetBean() {
        Assertions.assertThat(SpringContextUtil.getApplicationContext()).isNotNull();
        Assertions.assertThat(SpringContextUtil.getBean(IntegrationTestController.class)).isNotNull();
    }

}