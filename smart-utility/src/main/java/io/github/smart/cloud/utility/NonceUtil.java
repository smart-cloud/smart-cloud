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

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 全局交易流水号工具类
 *
 * <p>
 * <b>结构如下：</b>41(timestamp)+16(ip)+18(pid)+12(seq)
 * <p>
 * 改造雪花算法，将ip作为机器标志，进程号作为数据中心
 *
 * @author collin
 * @date 2019-04-06
 */
@Slf4j
public final class NonceUtil {

    /**
     * 起始的时间戳
     */
    private static final long START_STMP = 1480166465631L;

    // ----每一部分占用的位数 start
    /**
     * 序列号占用的位数
     */
    private static final long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private static final long IP_BIT = 16;
    /**
     * 数据中心占用的位数
     */
    private static final long PID_BIT = 18;
    // ----每一部分占用的位数 end

    /**
     * 每一部分的最大值
     */
    private static final long MAX_PID_NUM = -1L ^ (-1L << PID_BIT);
    private static final long MAX_IP_NUM = -1L ^ (-1L << IP_BIT);
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long IP_LEFT = SEQUENCE_BIT;
    private static final long PID_LEFT = SEQUENCE_BIT + IP_BIT;
    private static final long TIMESTMP_LEFT = PID_LEFT + PID_BIT;

    /**
     * 数据中心
     */
    private long ip;
    /**
     * 机器标识
     */
    private long pid;

    /**
     * 序列号
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private volatile long lastStmp = -1L;

    private static NonceUtil idWorker = new NonceUtil();

    public static NonceUtil getInstance() {
        return idWorker;
    }

    private NonceUtil() {
        String[] ipArray = null;
        try {
            ipArray = InetAddress.getLocalHost().getHostAddress().split("\\.");
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }

        ip = Long.valueOf(ipArray[2]) << 8 | Long.valueOf(ipArray[3]) & MAX_IP_NUM;
        pid = Long.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]) & MAX_PID_NUM;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized String nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;
        long id = // 时间戳部分
                (currStmp - START_STMP) << TIMESTMP_LEFT
                        // 数据中心部分
                        | ip << IP_LEFT
                        // 机器标识部分
                        | pid << PID_LEFT
                        // 序列号部分
                        | sequence;

        return Long.toHexString(id);
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

}