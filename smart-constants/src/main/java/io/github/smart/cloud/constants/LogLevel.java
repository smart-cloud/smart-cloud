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
package io.github.smart.cloud.constants;

/**
 * 日志级别
 *
 * @author collin
 * @date 2022-02-16
 */
public class LogLevel {

    /**
     * debug级别
     */
    public static final String DEBUG = "debug";

    /**
     * info级别
     */
    public static final String INFO = "info";

    /**
     * warn级别
     */
    public static final String WARN = "warn";

    private LogLevel() {
    }

    /**
     * 获取两个日志级别的最终结果
     *
     * @param annotationLevel 注解上面的日志级别
     * @param globalLevel     全局配置的日志级别
     * @return
     */
    public static final String getFinalLevel(String annotationLevel, String globalLevel) {
        if (annotationLevel != null && !"".equals(annotationLevel)) {
            return annotationLevel;
        }

        if (WARN.equals(annotationLevel) || WARN.equals(globalLevel)) {
            return WARN;
        }
        if (INFO.equals(annotationLevel) || INFO.equals(globalLevel)) {
            return INFO;
        }

        return DEBUG;
    }

}