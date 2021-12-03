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
