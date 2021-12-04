/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.mask.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.mask.util.LogUtil;

class LogUtilTest {

    private final int LOG_MAX_LENGTH = 2048;

    @Test
    void testTruncateFormat() {
        StringBuilder context = new StringBuilder(4096);
        for (int i = 0; i < LOG_MAX_LENGTH; i++) {
            context.append("0");
        }
        Assertions.assertThat(LogUtil.truncate(context.append("{}").toString(), "xxxxx").length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);
    }

    @Test
    void testTruncate() {
        StringBuilder context = new StringBuilder(4096);
        for (int i = 0; i < LOG_MAX_LENGTH; i++) {
            context.append("0");
        }
        Assertions.assertThat(LogUtil.truncate(context.toString()).length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);
        context.append("000000000");
        Assertions.assertThat(LogUtil.truncate(context.toString()).length()).isLessThanOrEqualTo(LOG_MAX_LENGTH);

        Assertions.assertThat(LogUtil.truncate(null)).isNull();
    }

    /**
     * 测试异常分支
     */
    @Test
    void testTruncateWithException() {
        Assertions.assertThatThrownBy(() -> {
            LogUtil.truncate(-1, "");
        }).isInstanceOf(IllegalArgumentException.class);
    }

}