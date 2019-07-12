package org.smartframework.cloud.utility.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.utility.test.integration.HttpUtilIntegrationTest;
import org.smartframework.cloud.utility.test.unit.AesUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.ArrayUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.CollectionUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.DateUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.JAXBUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.JasyptUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.Md5UtilUnitTest;
import org.smartframework.cloud.utility.test.unit.MockitoUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.NonceUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.ObjectUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.RandomUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.RsaUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.SecureRandomUtilUnitTest;
import org.smartframework.cloud.utility.test.unit.SystemUtilUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ HttpUtilIntegrationTest.class, AesUtilUnitTest.class, ArrayUtilUnitTest.class,
		CollectionUtilUnitTest.class, DateUtilUnitTest.class, JasyptUtilUnitTest.class, JAXBUtilUnitTest.class,
		Md5UtilUnitTest.class, MockitoUtilUnitTest.class, NonceUtilUnitTest.class, ObjectUtilUnitTest.class,
		RandomUtilUnitTest.class, RsaUtilUnitTest.class, SecureRandomUtilUnitTest.class, SystemUtilUnitTest.class })
public class SuiteTest {

}