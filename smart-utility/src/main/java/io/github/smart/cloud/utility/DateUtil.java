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
package io.github.smart.cloud.utility;

import io.github.smart.cloud.utility.constant.DateFormatterConst;
import io.github.smart.cloud.utility.constant.DatePatternConst;
import io.github.smart.cloud.utility.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 时间日期工具类
 *
 * @author collin
 * @date 2019-03-23
 */
@Slf4j
public class DateUtil {

    private DateUtil() {
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long currentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前日期（yyyy-MM-dd）
     *
     * @return
     */
    public static String getCurrentDate() {
        return LocalDateTime.now().format(DateFormatterConst.DATE);
    }

    /**
     * 获取当前日期（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateFormatterConst.DATETIME);
    }

    /**
     * 获取当前日期
     *
     * @param format 格式
     * @return
     */
    public static String getCurrentDateTime(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        return LocalDateTime.ofInstant(date.toInstant(), Holder.getZoneId())
                .format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 格式化日期
     *
     * @param currentMillis
     * @param format
     * @return
     */
    public static String format(long currentMillis, String format) {
        return format(toDate(currentMillis), format);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return 返回格式：yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return format(date, DatePatternConst.DATE);
    }

    /**
     * 格式化日期
     *
     * @param currentMillis
     * @return 返回格式：yyyy-MM-dd
     */
    public static String formatDate(long currentMillis) {
        return format(toDate(currentMillis), DatePatternConst.DATE);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        return format(date, DatePatternConst.DATETIME);
    }

    /**
     * 格式化日期
     *
     * @param currentMillis
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(long currentMillis) {
        return format(toDate(currentMillis), DatePatternConst.DATETIME);
    }

    /**
     * 日期字符串转LocalDateTime对象
     *
     * <h3>支持的字符串格式（{@link DatePatternConst}）</h3>
     * <ul>
     * <li>yyyy
     * <li>yyyy-MM
     * <li>yyyy-MM-dd
     * <li>yyyy-MM-dd HH
     * <li>yyyy-MM-dd HH:mm
     * <li>yyyy-MM-dd HH:mm:ss
     * </ul>
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static LocalDateTime parse(String dateStr) {
        Assert.hasText(dateStr, "date cannot be empty");

        int length = dateStr.length();
        DateTimeFormatter dateTimeFormatter = Holder.getDateTimeFormatterRouter().get(length);
        if (dateTimeFormatter == null) {
            throw new IllegalArgumentException(String.format("The format of [%s] is not supported！", dateStr));
        }

        return dateTimeFormatter.parse(dateStr, temporalAccessor -> {
            if (temporalAccessor.isSupported(ChronoField.HOUR_OF_DAY)) {
                return LocalDateTime.from(temporalAccessor);
            }

            if (temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH)) {
                return LocalDate.from(temporalAccessor).atStartOfDay();
            }
            if (temporalAccessor.isSupported(ChronoField.MONTH_OF_YEAR)) {
                return YearMonth.from(temporalAccessor).atDay(1).atStartOfDay();
            }
            if (temporalAccessor.isSupported(ChronoField.YEAR)) {
                return Year.from(temporalAccessor).atDay(1).atStartOfDay();
            }

            throw new IllegalArgumentException(String.format("The format of [%s] is not supported！", dateStr));
        });
    }

    /**
     * 日期字符串转毫秒
     *
     * <h3>支持的字符串格式（{@link DatePatternConst}）</h3>
     * <ul>
     * <li>yyyy
     * <li>yyyy-MM
     * <li>yyyy-MM-dd
     * <li>yyyy-MM-dd HH
     * <li>yyyy-MM-dd HH:mm
     * <li>yyyy-MM-dd HH:mm:ss
     * <li>yyyy-MM-dd HH:mm:ss.SSS
     * </ul>
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static long toCurrentMillis(String dateStr) {
        LocalDateTime localDateTime = parse(dateStr);
        return localDateTime.atZone(Holder.getZoneId()).toInstant().toEpochMilli();
    }

    /**
     * 时间戳转Date
     *
     * @param currentMillis
     * @return
     */
    public static Date toDate(long currentMillis) {
        return new Date(currentMillis);
    }

    /**
     * 获取从当前时间开始到当天截止时间为止的毫秒数
     *
     * @return
     */
    public static long getDurationBetweenNowAndTodayEnd() {
        LocalDateTime tomorrowBegin = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, tomorrowBegin).toMillis();
    }

    public static class Holder {
        /**
         * 所有的日期格式
         */
        private static final Map<Integer, DateTimeFormatter> DATETIME_FORMATTER_ROUTER = new HashMap<>();
        private static ZoneId zoneId;
        private static final String SMART_ZONEID_KEY = "smart.zoneId";
        private static final String ENV_NAME = "org.springframework.core.env.ConfigurableEnvironment";

        static {
            initDatetimeFormatterRouter();
            try {
                initZoneId();
            } catch (Exception e) {
                log.error("init zoneid fail", e);
            }
        }

        private Holder() {
        }

        /**
         * 初始化时间格式路由器
         */
        private static void initDatetimeFormatterRouter() {
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.YYYY.length(), DateFormatterConst.YYYY);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.YYYY_MM.length(), DateFormatterConst.YYYY_MM);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.DATE.length(), DateFormatterConst.DATE);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.DATE_HH.length(), DateFormatterConst.DATE_HH);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.DATE_HH_MM.length(), DateFormatterConst.DATE_HH_MM);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.DATETIME.length(), DateFormatterConst.DATETIME);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.DATETIME_SSS.length(), DateFormatterConst.DATETIME_SSS);
            DATETIME_FORMATTER_ROUTER.put(DatePatternConst.UTC.length(), DateFormatterConst.UTC);
        }

        /**
         * 初始化ZoneId（优先从配置文件中取时区配置）
         */
        private static void initZoneId() {
            boolean isPresent = ClassUtils.isPresent(ENV_NAME, Holder.class.getClassLoader());
            if (!isPresent) {
                zoneId = ZoneId.systemDefault();
                return;
            }

            ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
            if (applicationContext == null) {
                log.warn("ApplicationContext is null!!! The default zone id will be got instead of the zone id configed!!!");
                zoneId = ZoneId.systemDefault();
                return;
            }
            ConfigurableEnvironment environment = applicationContext.getBean(ConfigurableEnvironment.class);
            String id = environment.getProperty(SMART_ZONEID_KEY);
            if (StringUtils.hasText(id)) {
                zoneId = TimeZone.getTimeZone(id).toZoneId();
            } else {
                log.warn("'{}' is not configed!!! The default zone id will be got instead of the zone id configed!!!", SMART_ZONEID_KEY);
                zoneId = ZoneId.systemDefault();
            }
        }

        public static Map<Integer, DateTimeFormatter> getDateTimeFormatterRouter() {
            return DATETIME_FORMATTER_ROUTER;
        }

        public static ZoneId getZoneId() {
            return zoneId;
        }
    }

}