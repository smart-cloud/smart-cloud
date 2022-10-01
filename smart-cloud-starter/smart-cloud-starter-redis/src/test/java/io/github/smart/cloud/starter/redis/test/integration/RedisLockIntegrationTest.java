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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.smart.cloud.exception.AcquiredLockFailException;
import io.github.smart.cloud.starter.redis.test.prepare.controller.RedisLockController;
import io.github.smart.cloud.starter.redis.test.prepare.vo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

class RedisLockIntegrationTest extends AbstractRedisIntegrationTest {

    @Autowired
    private RedisLockController redisLockController;

    @Test
    void testWithKeyPrefix() {
        final Long uid = 1000000000000000000L;
        String mobile = "12345678901234567890";
        final String success = "ok";
        FutureTask<String> normalFutureTask = new FutureTask<>(() -> {
            User user = new User();
            user.setId(uid);
            user.setMobile("18700000000");
            try {
                redisLockController.testWithKeyPrefix(user, mobile);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return success;
        });

        FutureTask<String> acquireLockFailFutureTask = new FutureTask<>(() -> {
            User user = new User();
            user.setId(uid);
            user.setMobile("18700000000");
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
                redisLockController.testWithKeyPrefix(user, mobile);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return success;
        });
        new Thread(normalFutureTask).start();
        new Thread(acquireLockFailFutureTask).start();
        try {
            Assertions.assertThat(normalFutureTask.get()).isEqualTo(success);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Throwable throwable = null;
        try {
            acquireLockFailFutureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throwable = e.getCause();
        }
        Assertions.assertThat(throwable)
                .isNotNull()
                .isInstanceOf(AcquiredLockFailException.class);
    }

    @Test
    void testWithoutKeyPrefix() {
        User user = new User();
        user.setId(100L);
        user.setMobile("18700000000");
        redisLockController.testWithoutKeyPrefix(user, "13112341234");
    }

}
