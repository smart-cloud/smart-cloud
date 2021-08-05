package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.RetryTimeUtil;

class RetryTimeUtilTest {

    @Test
    void testGetNextExecuteTime() {
        int count = RetryTimeUtil.NEXT_EXECUTE_TIMES.length;
        for (int i = 0; i < count; i++) {
            Assertions.assertThat(RetryTimeUtil.getNextExecuteTime(i)).isEqualTo(RetryTimeUtil.NEXT_EXECUTE_TIMES[i]);
        }
        Assertions.assertThat(RetryTimeUtil.getNextExecuteTime(count + 100)).isEqualTo(RetryTimeUtil.NEXT_EXECUTE_TIMES[count - 1]);
    }

}
