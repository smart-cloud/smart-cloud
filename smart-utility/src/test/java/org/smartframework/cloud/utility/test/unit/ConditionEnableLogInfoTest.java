package org.smartframework.cloud.utility.test.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.spring.condition.ConditionEnableLogInfo;

class ConditionEnableLogInfoTest {

    @Test
    void test() {
        ConditionEnableLogInfo conditionEnableLogInfo = new ConditionEnableLogInfo();
        Assertions.assertTrue(conditionEnableLogInfo.matches(null, null));
    }

}