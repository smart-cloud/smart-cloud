package org.smartframework.cloud.starter.log.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.log.log4j2.plugin.CustomizeContextMapLookup;

public class CustomizeContextMapLookupUnitTest {

    @Test
    public void testReadAppName() {
        CustomizeContextMapLookup customizeContextMapLookup = new CustomizeContextMapLookup();
        Assertions.assertThat(customizeContextMapLookup.lookup("appName")).isEqualTo("smart-cloud-starter-log");
    }

}