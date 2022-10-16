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
package io.github.smart.cloud.starter.redis.test.unit;

import io.github.smart.cloud.starter.redis.RedisKeyUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RedisKeyUtilUnitTest {

    @Test
    void testBuildKeyAboutUnsupportedOperationException() {
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> RedisKeyUtil.buildKey(null));
        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> RedisKeyUtil.buildKey(new String[0]));
    }

    @Test
    void testBuildKey() {
        //--------count=1
        Assertions.assertThat(RedisKeyUtil.buildKey("1")).isEqualTo("1");
        Assertions.assertThat(RedisKeyUtil.buildKey(":1")).isEqualTo("1");
        Assertions.assertThat(RedisKeyUtil.buildKey("1:")).isEqualTo("1");

        //--------count=2
        String expectedValue2 = "1:2";
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1", "2:")).isEqualTo(expectedValue2);

        //--------count>2
        String expectedValue3 = "1:2:3";
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2", ":3")).isEqualTo(expectedValue3);


        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2:", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey(":1:", ":2:", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2:", ":3:")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1:", ":2:", ":3:")).isEqualTo(expectedValue3);
    }

}