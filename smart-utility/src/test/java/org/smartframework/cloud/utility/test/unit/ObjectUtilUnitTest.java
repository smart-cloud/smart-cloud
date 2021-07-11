package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.ObjectUtil;

class ObjectUtilUnitTest {

    @Test
    void testIsNull() {
        Assertions.assertThat(ObjectUtil.isNull(null)).isTrue();
        Assertions.assertThat(ObjectUtil.isNull("")).isFalse();
    }

    @Test
    void testIsNotNull() {
        Assertions.assertThat(ObjectUtil.isNotNull(null)).isFalse();
        Assertions.assertThat(ObjectUtil.isNotNull("")).isTrue();
    }

    @Test
    void testIsAllNull() {
        Assertions.assertThat(ObjectUtil.isAllNull(null, null)).isTrue();
        Assertions.assertThat(ObjectUtil.isAllNull(null, "")).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNull("", "")).isFalse();
    }

    @Test
    void testIsAllNotNull() {
        Assertions.assertThat(ObjectUtil.isAllNotNull(null, null)).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNotNull(null, "")).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNotNull("", "")).isTrue();
    }

    @Test
    void testEquals() {
        Assertions.assertThat(ObjectUtil.equals("123", "123")).isTrue();
        Assertions.assertThat(ObjectUtil.equals("1234", "123")).isFalse();
    }

}