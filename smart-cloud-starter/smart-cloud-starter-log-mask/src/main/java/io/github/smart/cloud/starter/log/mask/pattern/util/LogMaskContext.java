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
package io.github.smart.cloud.starter.log.mask.pattern.util;

import io.github.smart.cloud.starter.log.mask.pattern.PatternConfig;

import java.util.regex.Pattern;

/**
 * 日志脱敏上下文
 *
 * @author collin.li
 * @date 2025-12-23
 */
public class LogMaskContext {

    public static final ThreadLocal<Pattern> CONTEXT = new ThreadLocal<>();


    public static void set(Pattern pattern) {
        CONTEXT.set(pattern);
    }

    /**
     * 为当前上下文设置匹配的正则表达式
     *
     * @param regex
     */
    public static void set(String regex) {
        CONTEXT.set(PatternConfig.convert(regex));
    }

    /**
     * 获取当前上下文的正则匹配
     *
     * @return
     */
    public static Pattern get() {
        return CONTEXT.get();
    }

    /**
     * 清理上下文
     */
    public static void remove() {
        CONTEXT.remove();
    }

}