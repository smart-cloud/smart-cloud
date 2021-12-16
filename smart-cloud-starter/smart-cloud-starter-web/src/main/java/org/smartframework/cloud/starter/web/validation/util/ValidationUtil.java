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
package org.smartframework.cloud.starter.web.validation.util;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.exception.ParamValidateException;
import org.smartframework.cloud.starter.web.enums.WebReturnCodes;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;
import org.smartframework.cloud.starter.web.validation.ValidatorSingleton;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验
 *
 * @author collin
 * @date 2019-05-01
 */
public class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * 参数校验
     *
     * @param object
     */
    public static void validate(Object object) {
        if (object == null) {
            throw new ParamValidateException(WebReturnCodes.VALIDATE_IN_PARAMS_NULL);
        }

        Validator validator = ValidatorSingleton.getInstance();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(object);
        // 抛出检验异常
        if (CollectionUtils.isNotEmpty(constraintViolationSet)) {
            Set<ConstraintViolation<?>> constraintViolationSetTmp = constraintViolationSet.stream()
                    .map(item -> (ConstraintViolation<?>) (item)).collect(Collectors.toSet());

            String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSetTmp);
            throw new ParamValidateException(errorMsg);
        }
    }

}