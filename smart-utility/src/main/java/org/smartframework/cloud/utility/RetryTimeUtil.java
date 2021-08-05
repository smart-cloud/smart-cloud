package org.smartframework.cloud.utility;

/**
 * 重试时间工具类
 *
 * @author collin
 * @date 2021-06-30
 */
public class RetryTimeUtil {

    /**
     * 重试间隔时间
     */
    public static final long[] NEXT_EXECUTE_TIMES = {30L, 60L, 120L, 300L, 600L, 1800L, 3600L, 3600 * 4L, 3600 * 12L, 3600 * 24L};

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
        int consumerTimes = retriedTimes;
        int nextExecuteTimeMaxIndex = NEXT_EXECUTE_TIMES.length - 1;
        if (consumerTimes > nextExecuteTimeMaxIndex) {
            return NEXT_EXECUTE_TIMES[nextExecuteTimeMaxIndex];
        }

        return NEXT_EXECUTE_TIMES[consumerTimes];
    }

}