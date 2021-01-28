package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.SystemUtil;

public class SystemUtilUnitTest {

    @Test
    public void testIsWindows() {
        Assertions.assertThat(SystemUtil.isWindows()).isTrue();
    }

    @Test
    public void testIsLinux() {
        Assertions.assertThat(SystemUtil.isLinux()).isFalse();
    }

    @Test
    public void testGetUserDir() {
        Assertions.assertThat(SystemUtil.getUserDir()).isNotBlank();
    }

}