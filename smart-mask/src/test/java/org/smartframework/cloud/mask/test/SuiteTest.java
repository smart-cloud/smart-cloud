package org.smartframework.cloud.mask.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.mask.test.unit.MaskSerializeTest;
import org.smartframework.cloud.mask.test.unit.MaskUtilTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ MaskSerializeTest.class, MaskUtilTest.class })
public class SuiteTest {

}