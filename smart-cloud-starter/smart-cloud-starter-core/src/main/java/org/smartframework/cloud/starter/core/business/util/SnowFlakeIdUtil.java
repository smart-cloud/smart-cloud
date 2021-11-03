package org.smartframework.cloud.starter.core.business.util;

import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.smartframework.cloud.starter.configure.properties.SmartProperties;
import org.smartframework.cloud.utility.ObjectUtil;
import org.smartframework.cloud.utility.spring.SpringContextUtil;

/**
 * Twitter_Snowflake
 *
 * <h2>SnowFlake的结构如下(每部分用-分开):</h2>
 * <p>0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * <ul>
 * <li>1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * <li>41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * <li>10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 * <li>12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号。12位不够用时强制得到新的时间前缀。
 * </ul>
 * <p>加起来刚好64位，为一个Long型。
 *
 * <h2>优缺点</h2>
 * <ul>
 * <li>优点：整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高。
 * <li>缺点：对系统时间的依赖性非常强，需关闭ntp的时间同步功能。
 * </ul>
 *
 * <h2>改造</h2>
 * <ul>
 * <li>将5位datacenterId和5位workerId合并为dataMachineId（10位的数据机器位）。
 * <li>dataMachineId通过分布式配置中心配置，默认为0.
 * </ul>
 *
 * @author collin
 * @date 2021-11-11
 */
public final class SnowFlakeIdUtil {
    /**
     * 起始的时间戳
     */
    private static final long START_STAMP = 1480166465631L;

    /**
     * 每一部分占用的位数（序列号占用的位数）
     */
    private static final long SEQUENCE_BIT = 12;
    /**
     * 每一部分占用的位数（数据机器位）
     */
    private static final long DATE_MACHINE_BIT = 10;

    /**
     * 每一部分的最大值
     */
    private static final long MAX_DATE_MACHINE_NUM = -1L ^ (-1L << DATE_MACHINE_BIT);
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long DATE_MACHINE_LEFT = SEQUENCE_BIT;
    private static final long TIMESTAMP_LEFT = DATE_MACHINE_LEFT + DATE_MACHINE_BIT;

    /**
     * 数据机器标识（通过分布式配置中心配置，默认为0）
     */
    private long dataMachineId;
    /**
     * 序列号
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private volatile long lastStamp = -1L;

    private static volatile SnowFlakeIdUtil idWorker = null;

    public static SnowFlakeIdUtil getInstance() {
        if (ObjectUtil.isNull(idWorker)) {
            synchronized (SnowFlakeIdUtil.class) {
                if (ObjectUtil.isNull(idWorker)) {
                    idWorker = new SnowFlakeIdUtil();
                }
            }
        }

        return idWorker;
    }

    private SnowFlakeIdUtil() {
        Long dataMachineIdL = SpringContextUtil.getBean(SmartProperties.class).getDataMachineId();
        if (ObjectUtil.isNull(dataMachineIdL)) {
            throw new IllegalArgumentException(SmartConstant.SMART_PROPERTIES_PREFIX + "."
                    + SmartProperties.PropertiesName.DATA_MACHINE_ID + "未配置！");
        }
        dataMachineId = dataMachineIdL;
        if (dataMachineId > MAX_DATE_MACHINE_NUM || dataMachineId < 0) {
            throw new IllegalArgumentException("dataMachineId 取值范围[0, " + MAX_DATE_MACHINE_NUM + "]");
        }
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("发生时钟回拨，拒绝生产id");
        }

        if (currStamp == lastStamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return  // 时间戳部分
                (currStamp - START_STAMP) << TIMESTAMP_LEFT
                        // 数据机器标识部分
                        | dataMachineId << DATE_MACHINE_LEFT
                        // 序列号部分
                        | sequence;
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

}