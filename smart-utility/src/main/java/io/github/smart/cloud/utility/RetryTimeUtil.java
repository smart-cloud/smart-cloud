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

    private RetryTimeUtil() {
    }

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