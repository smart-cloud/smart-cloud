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
 * 服务器异常
 *
 * @author collin
 * @date 2019-06-29
 */
public class ServerException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ServerException(String message) {
        super.setCode(CommonReturnCodes.SERVER_ERROR);
        super.setMessage(message);
    }

}