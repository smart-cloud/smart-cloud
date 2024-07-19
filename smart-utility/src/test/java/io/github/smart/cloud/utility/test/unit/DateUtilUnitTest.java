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
import io.github.smart.cloud.utility.constant.DatePatternConst;
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
                .hasSameSizeAs(DatePatternConst.DATE);
    }

    @Test
    void testGetCurrentDateTime() {
        String currentDateStr = DateUtil.getCurrentDateTime();
        Assertions.assertThat(currentDateStr)
                .isNotBlank()
                .hasSameSizeAs(DatePatternConst.DATETIME);
    }

    @Test
    void testGetCurrentDateTimeWithFormat() {
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.YYYY)).hasSameSizeAs(DatePatternConst.YYYY);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.YYYY_MM)).hasSameSizeAs(DatePatternConst.YYYY_MM);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.DATE)).hasSameSizeAs(DatePatternConst.DATE);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.DATE_HH)).hasSameSizeAs(DatePatternConst.DATE_HH);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.DATE_HH_MM)).hasSameSizeAs(DatePatternConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.DATETIME)).hasSameSizeAs(DatePatternConst.DATETIME);
        Assertions.assertThat(DateUtil.getCurrentDateTime(DatePatternConst.DATETIME_SSS)).hasSameSizeAs(DatePatternConst.DATETIME_SSS);
    }

    @Test
    void testFormat() {
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.YYYY)).hasSameSizeAs(DatePatternConst.YYYY);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.YYYY_MM)).hasSameSizeAs(DatePatternConst.YYYY_MM);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.DATE)).hasSameSizeAs(DatePatternConst.DATE);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.DATE_HH)).hasSameSizeAs(DatePatternConst.DATE_HH);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.DATE_HH_MM)).hasSameSizeAs(DatePatternConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.DATETIME)).hasSameSizeAs(DatePatternConst.DATETIME);
        Assertions.assertThat(DateUtil.format(new Date(), DatePatternConst.DATETIME_SSS)).hasSameSizeAs(DatePatternConst.DATETIME_SSS);
    }

    @Test
    void testFormatMillis() {
        long currentMillis = new Date().getTime();
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.YYYY)).hasSameSizeAs(DatePatternConst.YYYY);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.YYYY_MM)).hasSameSizeAs(DatePatternConst.YYYY_MM);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.DATE)).hasSameSizeAs(DatePatternConst.DATE);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.DATE_HH)).hasSameSizeAs(DatePatternConst.DATE_HH);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.DATE_HH_MM)).hasSameSizeAs(DatePatternConst.DATE_HH_MM);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.DATETIME)).hasSameSizeAs(DatePatternConst.DATETIME);
        Assertions.assertThat(DateUtil.format(currentMillis, DatePatternConst.DATETIME_SSS)).hasSameSizeAs(DatePatternConst.DATETIME_SSS);
    }

    @Test
    void testFormatDate() {
        Assertions.assertThat(DateUtil.formatDate(new Date())).hasSameSizeAs(DatePatternConst.DATE);
    }

    @Test
    void testFormatDateMillis() {
        Assertions.assertThat(DateUtil.formatDate(new Date().getTime())).hasSameSizeAs(DatePatternConst.DATE);
    }

    @Test
    void testFormatDateTime() {
        Assertions.assertThat(DateUtil.formatDateTime(new Date())).hasSameSizeAs(DatePatternConst.DATETIME);
    }

    @Test
    void testFormatDateTimeMillis() {
        Assertions.assertThat(DateUtil.formatDateTime(new Date().getTime())).hasSameSizeAs(DatePatternConst.DATETIME);
    }

    @Test
    void testToDate() {
        String illegalDate = "20-11-23";
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            DateUtil.parse(illegalDate);
        }).withMessageContaining(String.format("The format of [%s] is not supported！", illegalDate));

        Assertions.assertThat(DateUtil.parse("2019")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01-01")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01-01 11")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01-01 11:11")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01-01 11:11:11")).isNotNull();
        Assertions.assertThat(DateUtil.parse("2019-01-01 11:11:11.111")).isNotNull();
    }

    @Test
    void testToCurrentMillis() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            DateUtil.toCurrentMillis(null);
        }).withMessageContaining("date cannot be empty");

        Date date = new Date();
        long ts1 = DateUtil.toCurrentMillis(DateUtil.format(date, DatePatternConst.DATETIME_SSS));
        long ts2 = date.getTime();
        Assertions.assertThat(ts1).isEqualTo(ts2);

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