package org.smartframework.cloud.starter.redis.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.redis.RedisKeyUtil;

class RedisKeyUtilUnitTest {

    @Test
    void testBuildKey() {
        //--------count=1
        Assertions.assertThat(RedisKeyUtil.buildKey("1")).isEqualTo("1");
        Assertions.assertThat(RedisKeyUtil.buildKey(":1")).isEqualTo("1");
        Assertions.assertThat(RedisKeyUtil.buildKey("1:")).isEqualTo("1");

        //--------count=2
        String expectedValue2 = "1:2";
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1", "2")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:")).isEqualTo(expectedValue2);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1", "2:")).isEqualTo(expectedValue2);

        //--------count>2
        String expectedValue3 = "1:2:3";
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2", ":3")).isEqualTo(expectedValue3);


        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2:", "3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", ":2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1", "2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", "2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2:", ":3")).isEqualTo(expectedValue3);

        Assertions.assertThat(RedisKeyUtil.buildKey(":1:", ":2:", ":3")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey("1:", ":2:", ":3:")).isEqualTo(expectedValue3);
        Assertions.assertThat(RedisKeyUtil.buildKey(":1:", ":2:", ":3:")).isEqualTo(expectedValue3);
    }

}