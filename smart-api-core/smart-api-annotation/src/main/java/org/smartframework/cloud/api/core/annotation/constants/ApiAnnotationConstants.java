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
package org.smartframework.cloud.api.core.annotation.constants;

/**
 * 接口注解常量
 *
 * @author collin
 * @date 2021-10-31
 */
public interface ApiAnnotationConstants {

    /**
     * 重复提交执行完后默认有效期
     */
    long DEFAULT_EXPIRE_MILLIS_OF_REPEAT_SUBMIT = 0L;

    /**
     * header 时间戳默认有效期（2分钟）
     */
    long DEFAULT_TIMESTAMP_VALID_MILLIS = 2 * 60 * 1000L;

}