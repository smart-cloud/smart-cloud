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

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.core.util.ResponseUtil;
import io.github.smart.cloud.starter.core.test.prepare.Application;
import io.github.smart.cloud.utility.spring.I18nUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ResponseUtilTest {

    @Test
    void testSuccess() {
        Response<Long> response = ResponseUtil.success();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.SUCCESS);
    }

    @Test
    void testSuccessWithBody() {
        Response<Long> response = ResponseUtil.success(1L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isNotBlank();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.SUCCESS);

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    void testIsSuccess() {
        Assertions.assertThat(ResponseUtil.isSuccess(ResponseUtil.error(CommonReturnCodes.VALIDATE_FAIL))).isFalse();
        Assertions.assertThat(ResponseUtil.isSuccess(ResponseUtil.success())).isTrue();
    }

    @Test
    void testGetFailMsg() {
        // Response is null
        Response<Long> nullResponse = null;
        String nullMsg = ResponseUtil.getFailMsg(nullResponse);
        Assertions.assertThat(nullMsg).isEqualTo(I18nUtil.getMessage(CommonReturnCodes.RPC_REQUEST_FAIL));

        // 正常分支
        Response<Long> errorResponse = ResponseUtil.error(CommonReturnCodes.VALIDATE_FAIL);
        String msg = "validate fail";
        errorResponse.setMessage(msg);

        String errorMsg = ResponseUtil.getFailMsg(errorResponse);
        Assertions.assertThat(errorMsg)
                .isNotBlank()
                .isEqualTo(msg);
    }

    @Test
    void testOfI18n() {
        Response response = ResponseUtil.ofI18n(CommonReturnCodes.UPLOAD_FILE_SIZE_EXCEEDED);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getCode()).isEqualTo(CommonReturnCodes.UPLOAD_FILE_SIZE_EXCEEDED);

        Assertions.assertThat(response.getTimestamp()).isPositive();
        Assertions.assertThat(response.getNonce()).isNotBlank();

        Assertions.assertThat(response.getMessage()).isNotBlank();
    }

}
