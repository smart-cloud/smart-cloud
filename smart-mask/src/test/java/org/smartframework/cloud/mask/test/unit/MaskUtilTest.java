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
import org.smartframework.cloud.mask.util.MaskUtil;

class MaskUtilTest {

    @Test
    void testMask() {
        Assertions.assertThat(MaskUtil.mask((Object) null, 3, 3, "***")).isEqualTo("***");
        Assertions.assertThat(MaskUtil.mask(null, 3, 3, "***")).isEqualTo("***");
        Assertions.assertThat(MaskUtil.mask("", 3, 3, "***")).isEqualTo("***");
        Assertions.assertThat(MaskUtil.mask("13112345678", 11, 3, "***")).isEqualTo("***");
        Assertions.assertThat(MaskUtil.mask("13112345678", 3, 10, "***")).isEqualTo("131***");
        Assertions.assertThat(MaskUtil.mask("13112345678", 3, 3, "***")).isEqualTo("131***678");
    }

}