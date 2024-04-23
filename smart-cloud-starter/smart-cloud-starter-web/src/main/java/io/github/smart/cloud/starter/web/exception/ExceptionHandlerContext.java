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
package io.github.smart.cloud.starter.web.exception;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.starter.core.business.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 异常处理工具
 *
 * @author collin
 * @date 2021-11-13
 */
@RequiredArgsConstructor
public class ExceptionHandlerContext {

    private final ExceptionHandlerStrategyFactory exceptionHandlerStrategyFactory;

    /**
     * 将{@link Throwable}解析构造{@link Response}
     *
     * @param e
     * @return
     */
    public Response transResponse(Throwable e) {
        for (IExceptionHandlerStrategy exceptionHandler : exceptionHandlerStrategyFactory.getExceptionHandlerStrategies()) {
            if (!exceptionHandler.match(e)) {
                continue;
            }

            Response response = exceptionHandler.trans(e);
            if (response != null) {
                return response;
            }
        }

        if (e != null) {
            String message = e.getMessage();
            if (StringUtils.isBlank(message)) {
                message = e.toString();
                // 只取异常类名
                int index = message.lastIndexOf(SymbolConstant.DOT);
                if (index != -1) {
                    message = message.substring(index + 1);
                }
            }

            return ResponseUtil.of(CommonReturnCodes.SERVER_ERROR, message);
        }

        return ResponseUtil.of(CommonReturnCodes.SERVER_ERROR, null);
    }

}