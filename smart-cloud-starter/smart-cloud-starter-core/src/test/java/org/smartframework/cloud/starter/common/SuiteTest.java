package org.smartframework.cloud.starter.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.starter.common.test.unit.PasswordUtilUnitTest;
import org.smartframework.cloud.starter.common.test.unit.ReflectionUtilUnitTest;
import org.smartframework.cloud.starter.common.test.unit.SmartSpringCloudApplicationConditionUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ PasswordUtilUnitTest.class, ReflectionUtilUnitTest.class,
		SmartSpringCloudApplicationConditionUnitTest.class })
public class SuiteTest {

}