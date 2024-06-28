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
package io.github.smart.cloud.starter.monitor.api.enums;

/**
 * 接口异常提醒类型
 *
 * @author collin
 * @date 2024-06-28
 */
public enum ApiExceptionRemindType {

    /**
     * 不需要提醒
     */
    NONE,
    /**
     * 失败率
     */
    FAIL_RATE,
    /**
     * 异常信息
     */
    EXCEPTION_INFO;

}