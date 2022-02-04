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
package org.smartframework.cloud.starter.web.exception;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.constants.CommonReturnCodes;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;

/**
 * 异常处理工具
 *
 * @author collin
 * @date 2021-11-13
 */
@UtilityClass
public class ExceptionHandlerContext {

    /**
     * 将{@link Throwable}解析构造{@link ResponseHead}
     *
     * @param e
     * @return
     */
    public static ResponseHead transRespHead(Throwable e) {
        for (IExceptionHandlerStrategy exceptionHandler : ExceptionHandlerStrategyFactory
                .getExceptionHandlerStrategys()) {
            if (!exceptionHandler.match(e)) {
                continue;
            }

            ResponseHead respHeadVO = exceptionHandler.transRespHead(e);
            if (respHeadVO != null) {
                return respHeadVO;
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

            return RespHeadUtil.of(CommonReturnCodes.SERVER_ERROR, message);
        }

        return RespHeadUtil.of(CommonReturnCodes.SERVER_ERROR, null);
    }

}