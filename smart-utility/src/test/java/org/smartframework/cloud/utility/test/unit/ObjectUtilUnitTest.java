package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.ObjectUtil;

public class ObjectUtilUnitTest {

    @Test
    public void testIsNull() {
        Assertions.assertThat(ObjectUtil.isNull(null)).isTrue();
        Assertions.assertThat(ObjectUtil.isNull("")).isFalse();
    }

    @Test
    public void testIsNotNull() {
        Assertions.assertThat(ObjectUtil.isNotNull(null)).isFalse();
        Assertions.assertThat(ObjectUtil.isNotNull("")).isTrue();
    }

    @Test
    public void testIsAllNull() {
        Assertions.assertThat(ObjectUtil.isAllNull(null, null)).isTrue();
        Assertions.assertThat(ObjectUtil.isAllNull(null, "")).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNull("", "")).isFalse();
    }

    @Test
    public void testIsAllNotNull() {
        Assertions.assertThat(ObjectUtil.isAllNotNull(null, null)).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNotNull(null, "")).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNotNull("", "")).isTrue();
    }

    @Test
    public void testEquals() {
        Assertions.assertThat(ObjectUtil.equals("123", "123")).isTrue();
        Assertions.assertThat(ObjectUtil.equals("1234", "123")).isFalse();
    }

}