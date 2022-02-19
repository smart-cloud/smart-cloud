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
package io.github.smart.cloud.starter.core.test.unit;

import io.github.smart.cloud.starter.core.business.util.RespUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.core.test.unit.prepare.Application;
import io.github.smart.cloud.utility.spring.I18nUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class RespUtilTest {

    @Test
    void testSuccess() {
        Response<Long> response = RespUtil.success();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
    }

    @Test
    void testSuccessWithBody() {
        Response<Long> response = RespUtil.success(1L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS);

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    void testIsSuccess() {
        Assertions.assertThat(RespUtil.isSuccess(RespUtil.error(CommonReturnCodes.VALIDATE_FAIL))).isFalse();
        Assertions.assertThat(RespUtil.isSuccess(RespUtil.success())).isTrue();
    }

    @Test
    void testGetFailMsg() {
        // Response is null
        Response<Long> nullResponse = null;
        String nullMsg = RespUtil.getFailMsg(nullResponse);
        Assertions.assertThat(nullMsg).isEqualTo(I18nUtil.getMessage(CommonReturnCodes.RPC_REQUEST_FAIL));

        // head is null
        Response<Long> headNullResponse = new Response<>();
        String headNullMsg = RespUtil.getFailMsg(headNullResponse);
        Assertions.assertThat(headNullMsg).isEqualTo(I18nUtil.getMessage(CommonReturnCodes.RPC_RESULT_EXCEPTION));

        // 正常分支
        Response<Long> errorResponse = RespUtil.error(CommonReturnCodes.VALIDATE_FAIL);
        String msg = "validate fail";
        errorResponse.getHead().setMessage(msg);

        String errorMsg = RespUtil.getFailMsg(errorResponse);
        Assertions.assertThat(errorMsg).isNotBlank();
        Assertions.assertThat(errorMsg).isEqualTo(msg);
    }

}
