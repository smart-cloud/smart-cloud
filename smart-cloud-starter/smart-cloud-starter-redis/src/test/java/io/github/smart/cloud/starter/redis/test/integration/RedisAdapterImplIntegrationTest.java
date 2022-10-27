/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.redis.test.integration;

import io.github.smart.cloud.starter.redis.adapter.IRedisAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class RedisAdapterImplIntegrationTest extends AbstractRedisIntegrationTest {

    @Autowired
    private IRedisAdapter redisAdapter;

    @BeforeEach
    void beforeTest() {
        Set<Object> keys = redisAdapter.keys("*");
        redisAdapter.delete(keys);
    }

    @Test
    void testSetString() {
        String key = "SetStringkey";
        String value = "SetStringvalue";
        redisAdapter.set(key, value, null);
        redisAdapter.set(key, value, 1000 * 60L);
        String expectedValue = (String) redisAdapter.get(key);
        Assertions.assertThat(value).isEqualTo(expectedValue);
    }

    @Test
    void testExpire() {
        String key = "Expire";
        String value = "Expire";
        redisAdapter.set(key, value, 1000 * 60L);
        String expectedValue1 = (String) redisAdapter.get(key);
        Assertions.assertThat(value).isEqualTo(expectedValue1);

        redisAdapter.expire(key, 0L);
        String expectedValue2 = (String) redisAdapter.get(key);
        Assertions.assertThat(expectedValue2).isBlank();
    }

    @Test
    void testDelete() {
        String key = "deletekey";
        String value = "deletevalue";
        redisAdapter.set(key, value, null);
        String expectedValue1 = (String) redisAdapter.get(key);
        Assertions.assertThat(value).isEqualTo(expectedValue1);

        Boolean result1 = redisAdapter.delete(key);
        Assertions.assertThat(result1).isTrue();

        Boolean result2 = redisAdapter.delete(key);
        Assertions.assertThat(result2).isFalse();

        String expectedValue2 = (String) redisAdapter.get(key);
        Assertions.assertThat(expectedValue2).isNull();
    }

    @Test
    void testBatchDelete() {
        String key1 = "batchdeletekey1";
        String value1 = "batchdeletevalue1";
        redisAdapter.set(key1, value1, null);
        String expectedValue1 = (String) redisAdapter.get(key1);
        Assertions.assertThat(value1).isEqualTo(expectedValue1);

        String key2 = "batchdeletekey2";
        String value2 = "batchdeletevalue2";
        redisAdapter.set(key2, value2, null);
        String expectedValue2 = (String) redisAdapter.get(key2);
        Assertions.assertThat(value2).isEqualTo(expectedValue2);

        Boolean result1 = redisAdapter.delete(Arrays.asList(key1, key2));
        Assertions.assertThat(result1).isTrue();

        Boolean result2 = redisAdapter.delete(Arrays.asList(key1, key2));
        Assertions.assertThat(result2).isFalse();

        String expectedValue12 = (String) redisAdapter.get(key1);
        String expectedValue22 = (String) redisAdapter.get(key2);
        Assertions.assertThat(expectedValue12).isNull();
        Assertions.assertThat(expectedValue22).isNull();
    }

    @Test
    void testSetNX() {
        String key = "SetNXkey";
        String value = "SetNXvalue";
        Boolean result1 = redisAdapter.setNx(key, value, 1000 * 60L);
        Assertions.assertThat(result1).isTrue();

        Boolean result2 = redisAdapter.setNx(key, value, 1000 * 60L);
        Assertions.assertThat(result2).isFalse();
    }

    @Test
    void testSetHash() {
        String key = "test:hash:user";
        String nameValue = "zhangsan";
        int heightValue = 160;

        Map<String, Object> data = new HashMap<>();
        data.put("name", nameValue);
        data.put("heign", heightValue);
        data.put("age", 20);
        data.put("islogin", true);

        Assertions.assertThat(redisAdapter.setHash(key, data, 3600)).isTrue();

        Map<Object, Object> cache = redisAdapter.getHash(key);
        Assertions.assertThat(cache).hasSize(data.size());

        String name = redisAdapter.get(key, "name");
        Assertions.assertThat(name).isEqualTo(nameValue);

        int heign = redisAdapter.get(key, "heign");
        Assertions.assertThat(heign).isEqualTo(heightValue);

        boolean islogin = redisAdapter.get(key, "islogin");
        Assertions.assertThat(islogin).isTrue();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class SetObject {
        private String name;
    }

}