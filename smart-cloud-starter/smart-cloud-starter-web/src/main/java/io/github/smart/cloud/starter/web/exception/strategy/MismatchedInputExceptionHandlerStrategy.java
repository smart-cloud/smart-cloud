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

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.starter.core.util.ResponseUtil;
import io.github.smart.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * json解析异常
 *
 * @author collin
 * @date 2023-02-20
 */
public class MismatchedInputExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return (e instanceof HttpMessageNotReadableException) && (((HttpMessageNotReadableException) e).getRootCause() instanceof MismatchedInputException);
    }

    @Override
    public Response trans(Throwable e) {
        return ResponseUtil.ofI18n(CommonReturnCodes.JSON_PARSE_ERROR);
    }

}