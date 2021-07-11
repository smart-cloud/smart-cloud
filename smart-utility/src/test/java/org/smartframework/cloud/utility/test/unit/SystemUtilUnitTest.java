package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.SystemUtil;

class SystemUtilUnitTest {

    @Test
    void testIsWindows() {
        Assertions.assertThat(SystemUtil.isWindows()).isTrue();
    }

    @Test
    void testIsLinux() {
        Assertions.assertThat(SystemUtil.isLinux()).isFalse();
    }

    @Test
    void testGetUserDir() {
        Assertions.assertThat(SystemUtil.getUserDir()).isNotBlank();
    }

}