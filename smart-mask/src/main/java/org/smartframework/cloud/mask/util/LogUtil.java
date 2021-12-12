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
package org.smartframework.cloud.mask.util;

import org.slf4j.helpers.MessageFormatter;

/**
 * 日志工具类
 *
 * @author collin
 * @date 2019-03-23
 */
public class LogUtil {

    /**
     * 日志最大长度
     */
    private static final int LOG_MAX_LENGTH = 2048;

    private LogUtil() {
    }

    public static String truncate(String format, Object... args) {
        return truncate(LOG_MAX_LENGTH, format, args);
    }

    public static String truncate(String msg) {
        return truncate(msg, LOG_MAX_LENGTH);
    }

    public static String truncate(int maxLength, String format, Object... args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = MaskUtil.mask(args[i]);
            }
        }

        String msg = MessageFormatter.format(format, args).getMessage();
        return truncate(msg, maxLength);
    }

    private static String truncate(final String str, final int maxWidth) {
        if (maxWidth < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        }

        if (str == null) {
            return null;
        }

        if (str.length() > maxWidth) {
            return str.substring(0, maxWidth);
        }

        return str;
    }

}