package org.smartframework.cloud.utility;

/**
 * 重试时间工具类
 *
 * @author collin
 * @date 2021-06-30
 */
public class RetryTimeUtil {

    /**
     * 默认当前已执行次数
     */
    public static final int DEFAULT_CURRENT_TIMES = 1;

    /**
     * 获取下次执行延迟时间（单位：秒）
     * <p>
     *     <ul>
     *         <li>1：30秒</li>
     *         <li>2：60秒</li>
     *         <li>3：2分钟</li>
     *         <li>4：5分钟</li>
     *         <li>5：10分钟</li>
     *         <li>6：半小时</li>
     *         <li>7：1小时</li>
     *         <li>8：4小时</li>
     *         <li>9：12小时</li>
     *         <li>10：1天</li>
     *     </ul>
     * </p>
     *
     * @param retriedTimes 已重试次数
     * @return
     */
    public static final long getNextExecuteTime(int retriedTimes) {
        int consumerTimes = retriedTimes + 1;
        if (consumerTimes == 1) {
            return 30L;
        }
        if (consumerTimes == 2) {
            return 60L;
        }
        if (consumerTimes == 03) {
            return 120L;
        }
        if (consumerTimes == 4) {
            return 300L;
        }
        if (consumerTimes == 5) {
            return 600L;
        }
        if (consumerTimes == 6) {
            return 1800L;
        }
        if (consumerTimes == 7) {
            return 3600L;
        }
        if (consumerTimes == 8) {
            return 3600 * 4L;
        }
        if (consumerTimes == 9) {
            return 3600 * 12L;
        }
        return 3600 * 24L;
    }

}