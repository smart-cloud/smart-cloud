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
package io.github.smart.cloud.starter.actuator.util;

import java.text.DecimalFormat;

/**
 * 百分比工具类
 *
 * @author collin
 * @date 2024-01-16
 */
public class PercentUtil {

    /**
     * 百分比格式
     */
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.00%");

    /**
     * 小数百分比格式化
     *
     * @param obj
     * @return
     */
    public static String format(Object obj) {
        synchronized (PERCENT_FORMAT) {
            return PERCENT_FORMAT.format(obj);
        }
    }

}