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
package org.smartframework.cloud.starter.web.exception.strategy;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

/**
 * @author collin
 * @desc 方法参数不合法异常转换
 * @date 2019/10/29
 */
public class MethodArgumentNotValidExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof MethodArgumentNotValidException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        // 参数校验
        List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        if (CollectionUtils.isNotEmpty(fieldErrors)) {
            String errorMsg = ExceptionUtil.getErrorMsg(fieldErrors);
            return RespHeadUtil.of(CommonReturnCodes.VALIDATE_FAIL, errorMsg);
        }
        return RespHeadUtil.of(CommonReturnCodes.VALIDATE_FAIL, e.getMessage());
    }

}