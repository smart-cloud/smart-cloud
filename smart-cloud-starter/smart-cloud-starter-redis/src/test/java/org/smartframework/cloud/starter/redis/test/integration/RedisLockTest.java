package org.smartframework.cloud.starter.redis.test.integration;

import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.redis.test.prepare.controller.RedisLockController;
import org.smartframework.cloud.starter.redis.test.prepare.vo.User;
import org.springframework.beans.factory.annotation.Autowired;

class RedisLockTest extends AbstractRedisIntegrationTest {

    @Autowired
    private RedisLockController redisLockController;

    @Test
    void test() {
        User user = new User();
        user.setId(100L);
        user.setMobile("18700000000");
        redisLockController.test(user, "13112341234");
    }

}
