package org.smartframework.cloud.starter.rpc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.starter.rpc.test.unit.SerializingUtilUnitTest;
import org.smartframework.cloud.starter.rpc.test.unit.SmartFeignClientConditionUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SmartFeignClientConditionUnitTest.class, SerializingUtilUnitTest.class})
public class SuiteTest {

}