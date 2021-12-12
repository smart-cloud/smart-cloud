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
package org.smartframework.cloud.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 重复提交校验异常
 *
 * @author collin
 * @date 2019-07-03
 */
public class RepeatSubmitException extends BaseException {

    private static final long serialVersionUID = 1L;

    public RepeatSubmitException() {
        setCode(CommonReturnCodes.REPEAT_SUBMIT.getCode());
    }

    public RepeatSubmitException(IBaseReturnCodes returnCodes) {
        setCode(returnCodes.getCode());
    }

}