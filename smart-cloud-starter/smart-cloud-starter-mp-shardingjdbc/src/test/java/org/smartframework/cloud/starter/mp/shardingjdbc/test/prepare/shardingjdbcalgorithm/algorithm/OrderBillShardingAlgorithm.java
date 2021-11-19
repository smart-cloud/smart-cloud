package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcalgorithm.util.OrderUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public void init() {

    }

    @Override
    public String getType() {
        return "ORDER_BILL_TABLE_TYPE";
    }

}