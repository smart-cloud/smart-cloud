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

import io.github.smart.cloud.utility.security.AesUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AesUtilUnitTest {

	@Test
	void testEncryptAndDecrypt() {
		String password = "123456";
		String plainText = "hello world!";
		String encryptText = AesUtil.encrypt(plainText, password);
		String decryptText = AesUtil.decrypt(encryptText, password);
		Assertions.assertThat(plainText).isEqualTo(decryptText);
	}
	
}