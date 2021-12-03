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
package org.smartframework.cloud.starter.log4j2.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扩展属性
 *
 * @author collin
 * @date 2021-10-30
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExtProperty {

    /**
     * 应用名
     */
    APP_NAME("appName"),
    /**
     * 日志保存根路径
     */
    LOG_PATH("logPath");

    private String name;

}