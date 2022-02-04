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

import org.smartframework.cloud.constants.CommonReturnCodes;

/**
 * 获取锁失败异常
 *
 * @author collin
 * @date 2022-02-04
 */
public class AcquiredLockFailException extends BaseException {

    private static final long serialVersionUID = 1L;

    public AcquiredLockFailException() {
        super(CommonReturnCodes.GET_LOCK_FAIL);
    }

    public AcquiredLockFailException(String code) {
        super.setCode(code);
    }

}