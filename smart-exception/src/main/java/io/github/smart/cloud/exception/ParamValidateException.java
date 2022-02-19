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
package io.github.smart.cloud.exception;

import io.github.smart.cloud.constants.CommonReturnCodes;

/**
 * 参数校验错误
 *
 * @author collin
 * @date 2019-05-01
 */
public class ParamValidateException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ParamValidateException() {
        setCode(CommonReturnCodes.PARAMETERS_MISSING);
    }

    public ParamValidateException(String code) {
        setCode(code);
    }

    public ParamValidateException(String code, String message) {
        setCode(code);
        setMessage(message);
    }

}