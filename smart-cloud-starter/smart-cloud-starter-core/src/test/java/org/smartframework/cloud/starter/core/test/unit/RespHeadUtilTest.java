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
package org.smartframework.cloud.starter.core.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.constants.CommonReturnCodes;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.core.test.unit.prepare.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class RespHeadUtilTest {

    @Test
    void testOf() {
        ResponseHead responseHead = RespHeadUtil.of();
        Assertions.assertThat(responseHead).isNotNull();
        Assertions.assertThat(responseHead.getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
    }

    @Test
    void testOfWithCodeAndMessage() {
        String code = "001";
        String msg = "done";

        ResponseHead responseHead = RespHeadUtil.of(code, msg);
        Assertions.assertThat(responseHead).isNotNull();
        Assertions.assertThat(responseHead.getCode()).isEqualTo(code);
        Assertions.assertThat(responseHead.getMessage()).isEqualTo(msg);

        Assertions.assertThat(responseHead.getTimestamp()).isGreaterThan(0L);
        Assertions.assertThat(responseHead.getNonce()).isNotBlank();
    }

    @Test
    void testOfReturnCodeAndMessage() {
        // not null
        String msg = "xx";
        ResponseHead responseHeadWithNone = RespHeadUtil.of(CommonReturnCodes.GET_LOCK_FAIL, msg);
        Assertions.assertThat(responseHeadWithNone).isNotNull();
        Assertions.assertThat(responseHeadWithNone.getCode()).isEqualTo(CommonReturnCodes.GET_LOCK_FAIL);

        Assertions.assertThat(responseHeadWithNone.getTimestamp()).isGreaterThan(0L);
        Assertions.assertThat(responseHeadWithNone.getNonce()).isNotBlank();

        Assertions.assertThat(responseHeadWithNone.getMessage()).isEqualTo(msg);

        // null
        ResponseHead responseHeadWithNull = RespHeadUtil.of(CommonReturnCodes.GET_LOCK_FAIL, null);
        Assertions.assertThat(responseHeadWithNull).isNotNull();
        Assertions.assertThat(responseHeadWithNull.getCode()).isEqualTo(CommonReturnCodes.GET_LOCK_FAIL);

        Assertions.assertThat(responseHeadWithNull.getTimestamp()).isGreaterThan(0L);
        Assertions.assertThat(responseHeadWithNull.getNonce()).isNotBlank();

        Assertions.assertThat(responseHeadWithNull.getMessage()).isNull();
    }

    @Test
    void testOfI18n() {
        ResponseHead responseHead = RespHeadUtil.ofI18n(CommonReturnCodes.UPLOAD_FILE_SIZE_EXCEEDED);
        Assertions.assertThat(responseHead).isNotNull();
        Assertions.assertThat(responseHead.getCode()).isEqualTo(CommonReturnCodes.UPLOAD_FILE_SIZE_EXCEEDED);

        Assertions.assertThat(responseHead.getTimestamp()).isGreaterThan(0L);
        Assertions.assertThat(responseHead.getNonce()).isNotBlank();

        Assertions.assertThat(responseHead.getMessage()).isNotBlank();
    }

}