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
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.starter.core.business.util.RespUtil;
import org.smartframework.cloud.starter.core.test.unit.prepare.Application;
import org.smartframework.cloud.utility.spring.I18nUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, args = "--spring.profiles.active=common")
class RespUtilTest {

    @Test
    void testSuccess() {
        Response<Long> response = RespUtil.success();
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
    }

    @Test
    void testSuccessWithBody() {
        Response<Long> response = RespUtil.success(1L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    void testError() {
        // 枚举
        Response<Long> response1 = RespUtil.error(CommonReturnCodes.VALIDATE_FAIL);
        Assertions.assertThat(response1).isNotNull();
        Assertions.assertThat(response1.getHead()).isNotNull();
        Assertions.assertThat(response1.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response1.getHead().getCode()).isEqualTo(CommonReturnCodes.VALIDATE_FAIL.getCode());

        // Response
        Response<Long> errorResponse = RespUtil.error(CommonReturnCodes.VALIDATE_FAIL);
        String msg = "validate fail";
        errorResponse.getHead().setMessage(msg);
        Response<Long> response2 = RespUtil.error(errorResponse);
        Assertions.assertThat(response2).isNotNull();
        Assertions.assertThat(response2.getHead()).isNotNull();
        Assertions.assertThat(response2.getHead().getCode()).isNotBlank();
        Assertions.assertThat(response2.getHead().getCode()).isEqualTo(CommonReturnCodes.SERVER_ERROR.getCode());
        Assertions.assertThat(response2.getHead().getMessage()).isEqualTo(msg);


        // String
        String errorMsg = "xxx";
        Response<Long> strResponse = RespUtil.error(errorMsg);
        Assertions.assertThat(strResponse).isNotNull();
        Assertions.assertThat(strResponse.getHead()).isNotNull();
        Assertions.assertThat(strResponse.getHead().getCode()).isNotBlank();
        Assertions.assertThat(strResponse.getHead().getCode()).isEqualTo(CommonReturnCodes.SERVER_ERROR.getCode());
        Assertions.assertThat(strResponse.getHead().getMessage()).isEqualTo(errorMsg);
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
        Assertions.assertThat(nullMsg).isEqualTo(I18nUtil.getMessage(CommonReturnCodes.RPC_REQUEST_FAIL.getCode()));

        // head is null
        Response<Long> headNullResponse = new Response<>();
        String headNullMsg = RespUtil.getFailMsg(headNullResponse);
        Assertions.assertThat(headNullMsg).isEqualTo(I18nUtil.getMessage(CommonReturnCodes.RPC_RESULT_EXCEPTION.getCode()));

        // 正常分支
        Response<Long> errorResponse = RespUtil.error(CommonReturnCodes.VALIDATE_FAIL);
        String msg = "validate fail";
        errorResponse.getHead().setMessage(msg);

        String errorMsg = RespUtil.getFailMsg(errorResponse);
        Assertions.assertThat(errorMsg).isNotBlank();
        Assertions.assertThat(errorMsg).isEqualTo(msg);
    }

}
