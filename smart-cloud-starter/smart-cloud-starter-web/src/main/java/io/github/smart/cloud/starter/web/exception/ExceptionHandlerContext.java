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
import io.github.smart.cloud.starter.core.util.ResponseUtil;
import lombok.RequiredArgsConstructor;

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

        // 未分类异常可能包含 SQL、文件路径、凭据或内部类名等敏感信息，不能直接返回客户端。
        return ResponseUtil.of(CommonReturnCodes.SERVER_ERROR, null);
    }

}