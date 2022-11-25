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
package io.github.smart.cloud.starter.configure.constants;

/**
 * 公共常量定义
 *
 * @author collin
 * @date 2019-04-22
 */
public class ConfigureConstant {

    private ConfigureConstant() {
    }

    /**
     * 公共配置属性前缀
     */
    public static final String SMART_PROPERTIES_PREFIX = "smart";
    /**
     * rpc 日志打印开关配置name
     */
    public static final String FEIGN_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".feign.log.enable";
    /**
     * api 日志打印开关配置name
     */
    public static final String API_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".apiLog.enable";
    /**
     * api mock开关配置name
     */
    public static final String MOCK_API_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".mock.api";
    /**
     * method mock开关配置name
     */
    public static final String MOCK_METHOD_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".mock.method";
    /**
     * method log开关配置name
     */
    public static final String METHOD_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".methodLog.enable";
    /**
     * mybatis log开关配置name
     */
    public static final String MYBATIS_LOG_CONDITION_PROPERTY = SMART_PROPERTIES_PREFIX + ".mybatis.log.enable";

}