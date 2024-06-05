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
package io.github.smart.cloud.utility.test.unit;

import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.constant.DateFormartConst;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class DateUtilUnitTest {

    @Test
    void testNow() {
        Assertions.assertThat(DateUtil.now()).isNotNull();
    }

    @Test
    void testCurrentMillis() {
        long currentMillis = DateUtil.currentMillis();
        Assertions.assertThat(DateUtil.currentMillis()).isGreaterThanOrEqualTo(currentMillis);
    }

    @Test
    void testGetCurrentDate() {
        String currentDateStr = DateUtil.getCurrentDate();
        Assertions.assertThat(currentDateStr)
                .isNotBlank()
                .hasSameSizeAs(DateFormartConst.DATE);
    }

    @Test
    void testGetCurrentDateTime() {
        String currentDateStr = DateUtil.getCurrentDateTime();
        Assertions.assertThat(currentDateStr)
                .isNotBlank()
                .hasSameSizeAs(DateFormartConst.DATETIME);
    }

    @Test
    void testGetCurrentDateTimeWithFormat() {
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.YYYY)).hasSameSizeAs(DateFormartConst.YYYY);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.YYYY_MM)).hasSameSizeAs(DateFormartConst.YYYY_MM);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.DATE)).hasSameSizeAs(DateFormartConst.DATE);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.DATE_HH)).hasSameSizeAs(DateFormartConst.DATE_HH);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.DATE_HH_MM)).hasSameSizeAs(DateFormartConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.DATETIME)).hasSameSizeAs(DateFormartConst.DATETIME);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DateFormartConst.DATETIME_SSS)).hasSameSizeAs(DateFormartConst.DATETIME_SSS);
    }

    @Test
    void testFormat() {
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.YYYY)).hasSameSizeAs(DateFormartConst.YYYY);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.YYYY_MM)).hasSameSizeAs(DateFormartConst.YYYY_MM);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.DATE)).hasSameSizeAs(DateFormartConst.DATE);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.DATE_HH)).hasSameSizeAs(DateFormartConst.DATE_HH);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.DATE_HH_MM)).hasSameSizeAs(DateFormartConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.DATETIME)).hasSameSizeAs(DateFormartConst.DATETIME);
        Assertions.assertThat(DateUtil.format(new Date(), DateFormartConst.DATETIME_SSS)).hasSameSizeAs(DateFormartConst.DATETIME_SSS);
    }

    @Test
    void testFormatMillis() {
        long currentMillis = new Date().getTime();
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.YYYY)).hasSameSizeAs(DateFormartConst.YYYY);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.YYYY_MM)).hasSameSizeAs(DateFormartConst.YYYY_MM);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.DATE)).hasSameSizeAs(DateFormartConst.DATE);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.DATE_HH)).hasSameSizeAs(DateFormartConst.DATE_HH);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.DATE_HH_MM)).hasSameSizeAs(DateFormartConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.DATETIME)).hasSameSizeAs(DateFormartConst.DATETIME);
        Assertions.assertThat(DateUtil.format(currentMillis, DateFormartConst.DATETIME_SSS)).hasSameSizeAs(DateFormartConst.DATETIME_SSS);
    }

    @Test
    void testFormatDate() {
        Assertions.assertThat(DateUtil.formatDate(new Date())).hasSameSizeAs(DateFormartConst.DATE);
    }

    @Test
    void testFormatDateMillis() {
        Assertions.assertThat(DateUtil.formatDate(new Date().getTime())).hasSameSizeAs(DateFormartConst.DATE);
    }

    @Test
    void testFormatDateTime() {
        Assertions.assertThat(DateUtil.formatDateTime(new Date())).hasSameSizeAs(DateFormartConst.DATETIME);
    }

    @Test
    void testFormatDateTimeMillis() {
        Assertions.assertThat(DateUtil.formatDateTime(new Date().getTime())).hasSameSizeAs(DateFormartConst.DATETIME);
    }

    @Test
    void testToDate() {
        Assertions.assertThat(DateUtil.toDate("2019")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01-01")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01-01 11")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11:11")).isNotNull();
        Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11:11.111")).isNotNull();
    }

    @Test
    void testToCurrentMillis() {
        Assertions.assertThat(DateUtil.toCurrentMillis(null)).isNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01-01")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01-01 11")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01-01 11:11")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01-01 11:11:11")).isNotNull();
        Assertions.assertThat(DateUtil.toCurrentMillis("2019-01-01 11:11:11.111")).isNotNull();
    }

    @Test
    void testToDateCurrentMillis() {
        Assertions.assertThat(DateUtil.toDate(new Date().getTime())).isNotNull();
    }

    @Test
    void testGetDurationBetweenNowAndTodayEnd() {
        long durationTs = DateUtil.getDurationBetweenNowAndTodayEnd();
        Assertions.assertThat(durationTs).isGreaterThanOrEqualTo(0);
    }

}