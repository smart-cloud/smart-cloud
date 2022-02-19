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

import io.github.smart.cloud.utility.SecureRandomUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import java.security.NoSuchAlgorithmException;

class SecureRandomUtilUnitTest {

    @RepeatedTest(16)
    void testGenerateRandom() throws NoSuchAlgorithmException {
        String random1 = SecureRandomUtil.generateRandom(true, 10);
        String random2 = SecureRandomUtil.generateRandom(false, 10);
        Assertions.assertThat(random1.length()).isEqualTo(10);
        Assertions.assertThat(random2.length()).isEqualTo(10);
    }

    @RepeatedTest(16)
    void testGenerateRangeRandom() throws NoSuchAlgorithmException {
        int random1 = SecureRandomUtil.generateRangeRandom(10, 100);
        Assertions.assertThat(random1).isBetween(10, 100);
    }

}