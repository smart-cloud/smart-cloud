package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.NonceUtil;

public class NonceUtilUnitTest {

    @Test
    public void test() {
        String id1 = NonceUtil.getInstance().nextId();
        Assertions.assertThat(id1).isNotBlank();

        String id2 = NonceUtil.getInstance().nextId();
        Assertions.assertThat(id2).isNotBlank();

        Assertions.assertThat(id1).isNotEqualTo(id2);
    }

}