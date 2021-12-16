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
package org.smartframework.cloud.starter.web.test.cases;

import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.exception.ParamValidateException;
import org.smartframework.cloud.starter.web.enums.WebReturnCodes;
import org.smartframework.cloud.starter.web.test.prepare.Application;
import org.smartframework.cloud.starter.web.validation.util.ValidationUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.NotBlank;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ValidationUtilTest {

    /**
     * 校验不通过（入参为null）
     */
    @Test
    void testNull() {
        Exception expectedException = null;
        try {
            ValidationUtil.validate(null);
        } catch (Exception e) {
            expectedException = e;
        }

        Assertions.assertThat(expectedException).isNotNull();
        Assertions.assertThat(expectedException instanceof ParamValidateException).isTrue();
        Assertions.assertThat(((ParamValidateException) expectedException).getCode()).isEqualTo(WebReturnCodes.VALIDATE_IN_PARAMS_NULL.getCode());
    }

    /**
     * 校验不通过（入参不为null）
     */
    @Test
    void testNotNull() {
        ProductReqVO productReqVO = new ProductReqVO();
        Exception expectedException = null;
        try {
            ValidationUtil.validate(productReqVO);
        } catch (Exception e) {
            expectedException = e;
        }

        Assertions.assertThat(expectedException).isNotNull();
        Assertions.assertThat(expectedException instanceof ParamValidateException).isTrue();
        Assertions.assertThat(((ParamValidateException) expectedException).getCode()).isEqualTo(CommonReturnCodes.PARAMETERS_MISSING.getCode());
    }

    /**
     * 校验通过
     */
    @Test
    void testValid() {
        ProductReqVO productReqVO = new ProductReqVO();
        productReqVO.setName("mobile");
        productReqVO.setDesc("手机");
        Exception expectedException = null;
        try {
            ValidationUtil.validate(productReqVO);
        } catch (Exception e) {
            expectedException = e;
        }

        Assertions.assertThat(expectedException).isNull();
    }

    @Getter
    @Setter
    class ProductReqVO extends Base {

        @NotBlank
        @Length(max = 64)
        private String name;

        @NotBlank
        @Length(max = 128)
        private String desc;

    }
}