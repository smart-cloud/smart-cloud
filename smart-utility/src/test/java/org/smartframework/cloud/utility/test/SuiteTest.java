package org.smartframework.cloud.utility.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.utility.test.unit.AesUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.RandomUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.RsaUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.SecureRandomUtilUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AesUtilUnitTest.class, RandomUtilUnitTest.class, RsaUtilUnitTest.class,
		SecureRandomUtilUnitTest.class })
public class SuiteTest {

}