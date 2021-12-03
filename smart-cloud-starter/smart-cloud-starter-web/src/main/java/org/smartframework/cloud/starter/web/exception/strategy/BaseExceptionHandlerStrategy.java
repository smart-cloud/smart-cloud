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

import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.exception.BaseException;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.utility.spring.I18nUtil;

/**
 * @author liyulin
 * @desc 自定义异常转换
 * @date 2019/10/29
 */
public class BaseExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof BaseException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        BaseException ex = (BaseException) e;
        return RespHeadUtil.of(ex.getCode(), I18nUtil.getMessage(ex.getMessage()));
    }

}