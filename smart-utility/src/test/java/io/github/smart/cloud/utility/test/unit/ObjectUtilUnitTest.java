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
package io.github.smart.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.smart.cloud.utility.ObjectUtil;

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
        Assertions.assertThat(ObjectUtil.isAllNull(null)).isTrue();
        Assertions.assertThat(ObjectUtil.isAllNull(null, null)).isTrue();
        Assertions.assertThat(ObjectUtil.isAllNull(null, "")).isFalse();
        Assertions.assertThat(ObjectUtil.isAllNull("", "")).isFalse();
    }

    @Test
    void testIsAllNotNull() {
        Assertions.assertThat(ObjectUtil.isAllNotNull(null)).isFalse();
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