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
package io.github.smart.cloud.starter.web.exception.strategy;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.core.util.ResponseUtil;
import io.github.smart.cloud.starter.web.exception.IExceptionHandlerStrategy;
import io.github.smart.cloud.starter.web.exception.util.ExceptionUtil;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 参数校验异常转换
 *
 * @author collin
 * @date 2019/10/29
 */
public class ConstraintViolationExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof ConstraintViolationException;
    }

    @Override
    public Response trans(Throwable e) {
        // 参数校验
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
        Set<ConstraintViolation<?>> constraintViolationSet = constraintViolationException.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolationSet)) {
            String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSet);
            return ResponseUtil.of(CommonReturnCodes.VALIDATE_FAIL, errorMsg);
        }

        return ResponseUtil.of(CommonReturnCodes.VALIDATE_FAIL, e.getMessage());
    }

}