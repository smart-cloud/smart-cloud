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
package io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import io.github.smart.cloud.utility.DateUtil;

/**
 * 订单工具类
 *
 * @author collin
 * @date 2021-02-08
 */
@Slf4j
public class OrderUtil {
    /**
     * 每个库的表数量（00~99）
     */
    private static final long DB_TABLE_NUM = 100L;

    /**
     * 每个库的数据量（0~99999999）
     */
    private static final long DB_DATA_LIMIT = 1_00_0000L * DB_TABLE_NUM;

    /**
     * 生成订单号
     *
     * @param uid
     * @return
     */
    public static String generateOrderNo(Long uid) {
        // yyyyMMddHHmmssSSS+uid/100000000+uid%100
        return new StringBuilder()
                .append(DateUtil.getCurrentDateTime("yyyyMMddHHmmssSSS"))
                .append(StringUtils.leftPad(String.valueOf(whichDB(uid)), 8, '0'))
                .append(StringUtils.leftPad(String.valueOf(whichTable(uid)), 2, '0')).toString();
    }

    public static final Long whichDB(Long uid) {
        return uid / DB_DATA_LIMIT;
    }

    public static final Long whichTable(Long uid) {
        return uid % DB_TABLE_NUM;
    }

    public static final Long whichDB(String orderNo) {
        return shardingByOrderNo(orderNo, orderNo.length() - 10, orderNo.length() - 2);
    }

    public static final Long whichTable(String orderNo) {
        return shardingByOrderNo(orderNo, orderNo.length() - 2, orderNo.length());
    }

    private static final Long shardingByOrderNo(String orderNo, int start, int end) {
        if (orderNo == null || orderNo.length() == 0) {
            return null;
        }
        try {
            String dbSuffixNum = StringUtils.substring(orderNo, start, end);
            return Long.valueOf(dbSuffixNum);
        } catch (Exception e) {
            log.error("invalid order id:{}, can't do sharing.", orderNo, e);
        }
        return null;
    }

}