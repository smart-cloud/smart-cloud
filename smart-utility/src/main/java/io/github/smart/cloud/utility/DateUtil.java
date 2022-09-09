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

import io.github.smart.cloud.utility.constant.DateFormartConst;
import io.github.smart.cloud.utility.spring.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormartConst.DATE));
    }

    /**
     * 获取当前日期（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormartConst.DATETIME));
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
        return format(date, DateFormartConst.DATE);
    }

    /**
     * 格式化日期
     *
     * @param currentMillis
     * @return 返回格式：yyyy-MM-dd
     */
    public static String formatDate(long currentMillis) {
        return format(toDate(currentMillis), DateFormartConst.DATE);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        return format(date, DateFormartConst.DATETIME);
    }

    /**
     * 格式化日期
     *
     * @param currentMillis
     * @return 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(long currentMillis) {
        return format(toDate(currentMillis), DateFormartConst.DATETIME);
    }

    /**
     * 日期字符串转Date对象
     *
     * <h3>支持的字符串格式（{@link DateFormartConst}）</h3>
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
    public static Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        int length = dateStr.length();
        String format = Holder.getDateTimeFormatterRouter().get(length);
        if (Objects.isNull(format)) {
            throw new IllegalArgumentException(String.format("The format of [%s] is not supported！", format));
        }

        Date date = null;
        try {
            date = DateUtils.parseDate(dateStr, format);
        } catch (ParseException e) {
            log.error("data string parse error", e);
        }
        return date;
    }

    /**
     * 日期字符串转Date对象
     *
     * <h3>支持的字符串格式（{@link DateFormartConst}）</h3>
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
    public static Long toCurrentMillis(String dateStr) {
        Date date = toDate(dateStr);
        if (date == null) {
            return null;
        }
        return date.getTime();
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

    static class Holder {
        /**
         * 所有的日期格式
         */
        private static final Map<Integer, String> DATETIME_FORMATTER_ROUTER = new HashMap<>();
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
            // 所有公有
            Field[] fields = DateFormartConst.class.getFields();
            // 日期格式变量前缀
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String format = null;
                try {
                    format = String.valueOf(field.get(field));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                }
                if (format != null) {
                    DATETIME_FORMATTER_ROUTER.put(format.length(), format);
                }
            }
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
            if (StringUtils.isBlank(id)) {
                log.warn("'{}' is not configed!!! The default zone id will be got instead of the zone id configed!!!",
                        SMART_ZONEID_KEY);
                zoneId = ZoneId.systemDefault();
            } else {
                zoneId = TimeZone.getTimeZone(id).toZoneId();
            }
        }

        protected static Map<Integer, String> getDateTimeFormatterRouter() {
            return DATETIME_FORMATTER_ROUTER;
        }

        protected static ZoneId getZoneId() {
            return zoneId;
        }
    }

}