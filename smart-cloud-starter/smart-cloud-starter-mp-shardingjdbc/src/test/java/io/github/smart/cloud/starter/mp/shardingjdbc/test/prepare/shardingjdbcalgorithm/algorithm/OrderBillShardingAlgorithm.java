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
package io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import io.github.smart.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.util.OrderUtil;

import java.util.*;

public class OrderBillShardingAlgorithm<T extends Comparable<?>> implements ComplexKeysShardingAlgorithm<T> {

    private static final String SHARDING_COLUMN_UID = "f_buyer";
    private static final String SHARDING_COLUMN_ORDER_NO = "f_order_no";

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<T> complexKeysShardingValue) {
        Set<String> targetTableNames = new HashSet<>();
        Map<String, Collection<T>> columnNameAndShardingValueMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        Collection<T> uidValues = columnNameAndShardingValueMap.get(SHARDING_COLUMN_UID);
        if (uidValues != null) {
            uidValues.stream().forEach(uid -> {
                Long uidSharding = OrderUtil.whichTable((Long) uid);
                targetTableNames.add(String.format("%s_%s", complexKeysShardingValue.getLogicTableName(), uidSharding));
            });
        }

        Collection<T> orderNoValues = columnNameAndShardingValueMap.get(SHARDING_COLUMN_ORDER_NO);
        if (orderNoValues != null) {
            orderNoValues.stream().forEach(orderNo -> {
                Long orderNoSharding = OrderUtil.whichTable((String) orderNo);
                targetTableNames.add(String.format("%s_%s", complexKeysShardingValue.getLogicTableName(), orderNoSharding));
            });
        }

        return targetTableNames;
    }

    @Override
    public String getType() {
        return "ORDER_BILL_TABLE_TYPE";
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}