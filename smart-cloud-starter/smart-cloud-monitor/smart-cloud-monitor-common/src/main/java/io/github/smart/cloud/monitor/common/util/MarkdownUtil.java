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
package io.github.smart.cloud.monitor.common.util;

/**
 * markdown工具类
 *
 * @author collin.li
 * @date 2026-07-31
 */
public final class MarkdownUtil {

    /**
     * markdown特殊字符集合
     */
    private static final String SPECIAL_CHARS = "\\`*_#>[]()~";
    /**
     * markdown转义字符
     */
    private static final char ESCAPE_CHAR = '\\';

    private MarkdownUtil() {
    }

    /**
     * 转义markdown特殊字符，避免动态内容破坏markdown格式
     *
     * @param text 待转义的文本
     * @return 转义后的文本
     */
    public static String escape(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        int length = text.length();
        StringBuilder escapedText = new StringBuilder(length + 16);
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (SPECIAL_CHARS.indexOf(c) >= 0) {
                escapedText.append(ESCAPE_CHAR);
            }
            escapedText.append(c);
        }
        return escapedText.toString();
    }

}
