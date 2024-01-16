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
        return PERCENT_FORMAT.format(obj);
    }

}