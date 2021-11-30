package org.smartframework.cloud.mask.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.mask.util.LogUtil;

class LogUtilTest {

    private final int LOG_MAX_LENGTH = 2048;

    @Test
    void testTruncateFormat() {
        StringBuilder context = new StringBuilder(4096);
        for (int i = 0; i < 2048; i++) {
            context.append("0");
        }
        Assertions.assertThat(LogUtil.truncate(context.append("{}").toString(), "xxxxx").length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);
    }

    @Test
    void testTruncate() {
        StringBuilder context = new StringBuilder(4096);
        for (int i = 0; i < 2048; i++) {
            context.append("0");
        }
        Assertions.assertThat(LogUtil.truncate(context.toString()).length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);
        context.append("000000000");
        Assertions.assertThat(LogUtil.truncate(context.toString()).length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);
    }

}