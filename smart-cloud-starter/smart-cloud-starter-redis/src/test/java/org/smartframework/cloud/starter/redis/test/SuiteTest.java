package org.smartframework.cloud.starter.redis.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.smartframework.cloud.starter.redis.test.integration.RedisComponentIntegrationTest;
import org.smartframework.cloud.starter.redis.test.unit.RedisKeyUtilUnitTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ RedisComponentIntegrationTest.class, RedisKeyUtilUnitTest.class})
public class SuiteTest {

}