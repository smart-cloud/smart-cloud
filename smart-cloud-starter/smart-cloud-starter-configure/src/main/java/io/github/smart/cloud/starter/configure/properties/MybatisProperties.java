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
package io.github.smart.cloud.starter.configure.properties;

import io.github.smart.cloud.constants.LogLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * api日志切面配置
 *
 * @author collin
 * @date 2019-06-19
 */
@Getter
@Setter
@ToString
public class MybatisProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * mybatis日志开关 （默认true）
     */
    private boolean enable = true;

    /**
     * api日志级别（默认DEBUG）
     *
     * @see LogLevel
     */
    private String logLevel = LogLevel.DEBUG;
    /**
     * 加解密密钥
     */
    private Map<String, String> cryptKeys = new LinkedHashMap<>();

}