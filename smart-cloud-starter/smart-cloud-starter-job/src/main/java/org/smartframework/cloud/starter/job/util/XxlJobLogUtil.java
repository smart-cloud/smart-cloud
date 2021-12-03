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
package org.smartframework.cloud.starter.job.util;

import com.xxl.job.core.context.XxlJobHelper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * xxl-job日志工具类（会在日志系统、xxl-job中同时写入）
 *
 * @author liyulin
 * @date 2019-7-11
 */
@UtilityClass
@Slf4j
public class XxlJobLogUtil {

    public static void debug(String format, Object... arguments) {
        log.debug(format, arguments);
        XxlJobHelper.log(format, arguments);
    }

    public static void info(String format, Object... arguments) {
        log.info(format, arguments);
        XxlJobHelper.log(format, arguments);
    }

    public static void warning(String format, Object... arguments) {
        log.warn(format, arguments);
        XxlJobHelper.log(format, arguments);
    }

    public static void error(String msg, Throwable e) {
        log.error(msg, e);
        XxlJobHelper.log(e);
    }

}