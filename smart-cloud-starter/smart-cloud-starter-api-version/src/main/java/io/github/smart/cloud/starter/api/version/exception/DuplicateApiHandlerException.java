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
package io.github.smart.cloud.starter.api.version.exception;

import io.github.smart.cloud.starter.api.version.core.ApiHandlerFactory;

/**
 * 接口handler名称重复异常异常
 *
 * @author collin
 * @date 2024-06-06
 * @see ApiHandlerFactory
 */
public class DuplicateApiHandlerException extends RuntimeException {

    public DuplicateApiHandlerException(String message) {
        super(message);
    }

}