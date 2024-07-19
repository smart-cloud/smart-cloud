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
package io.github.smart.cloud.utility.constant;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式
 *
 * @author collin
 * @date 2024-07-16
 */
public class DateFormatterConst {

    /**
     * yyyy
     */
    public static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern(DatePatternConst.YYYY);
    /**
     * yyyy-MM
     */
    public static final DateTimeFormatter YYYY_MM = DateTimeFormatter.ofPattern(DatePatternConst.YYYY_MM);
    /**
     * yyyy-MM-dd
     */
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern(DatePatternConst.DATE);
    /**
     * yyyy-MM-dd HH
     */
    public static final DateTimeFormatter DATE_HH = DateTimeFormatter.ofPattern(DatePatternConst.DATE_HH);
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final DateTimeFormatter DATE_HH_MM = DateTimeFormatter.ofPattern(DatePatternConst.DATE_HH_MM);
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern(DatePatternConst.DATETIME);
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final DateTimeFormatter DATETIME_SSS = DateTimeFormatter.ofPattern(DatePatternConst.DATETIME_SSS);
    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    public static final DateTimeFormatter UTC = DateTimeFormatter.ofPattern(DatePatternConst.UTC);

    private DateFormatterConst() {
    }

}