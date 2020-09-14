package org.smartframework.cloud.utility.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.utility.test.integration.HttpUtilIntegrationTest;
import org.smartframework.cloud.utility.test.unit.*;
import org.smartframework.cloud.utility.test.unit.protostuff.SerializingUtilUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({HttpUtilIntegrationTest.class, AesUtilUnitTest.class, DateUtilUnitTest.class,
        JasyptUtilUnitTest.class, JAXBUtilUnitTest.class, Md5UtilUnitTest.class, NonceUtilUnitTest.class,
        ObjectUtilUnitTest.class, PBEWithMD5AndDESUtilUnitTest.class, RandomUtilUnitTest.class,
        RsaUtilUnitTest.class, SecureRandomUtilUnitTest.class, SerializingUtilUnitTest.class, SystemUtilUnitTest.class})
public class SuiteTest {

}