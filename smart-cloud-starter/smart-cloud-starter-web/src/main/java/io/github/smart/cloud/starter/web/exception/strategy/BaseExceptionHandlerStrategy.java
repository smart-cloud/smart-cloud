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
import io.github.smart.cloud.exception.BaseException;
import io.github.smart.cloud.starter.core.business.util.ResponseUtil;
import io.github.smart.cloud.starter.web.exception.IExceptionHandlerStrategy;
import io.github.smart.cloud.utility.spring.I18nUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义异常转换
 *
 * @author collin
 * @date 2019/10/29
 */
public class BaseExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof BaseException;
    }

    @Override
    public Response trans(Throwable e) {
        if (e instanceof BaseException) {
            BaseException ex = (BaseException) e;
            String message = e.getMessage();
            if (StringUtils.isBlank(message)) {
                message = I18nUtil.getMessage(ex.getCode(), ex.getArgs());
            }
            return ResponseUtil.of(ex.getCode(), message);
        }

        return ResponseUtil.of(CommonReturnCodes.SERVER_ERROR, e.getMessage());
    }

}