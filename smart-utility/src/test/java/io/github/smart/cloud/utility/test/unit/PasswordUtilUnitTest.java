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

import io.github.smart.cloud.utility.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

@Slf4j
class PasswordUtilUnitTest {

    @Test
    void testSecure() throws NoSuchAlgorithmException {
        String result = PasswordUtil.secure("123456", PasswordUtil.generateRandomSalt());
        log.info("result={}", result);
        Assertions.assertThat(result).isNotBlank();
    }

    @Test
    void testGenerateRandomSalt() throws NoSuchAlgorithmException {
        String randomSalt = PasswordUtil.generateRandomSalt();
        Assertions.assertThat(randomSalt).hasSize(16);
    }

}