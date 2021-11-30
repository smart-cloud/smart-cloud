package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.SystemUtil;

class SystemUtilUnitTest {

    @Test
    void testOs() {
        boolean isLinux = SystemUtil.isLinux();
        boolean isWindows = SystemUtil.isWindows();
        Assertions.assertThat(isLinux || isWindows).isTrue();
        Assertions.assertThat(isLinux && isWindows).isFalse();
    }

    @Test
    void testGetUserDir() {
        Assertions.assertThat(SystemUtil.getUserDir()).isNotBlank();
    }

}