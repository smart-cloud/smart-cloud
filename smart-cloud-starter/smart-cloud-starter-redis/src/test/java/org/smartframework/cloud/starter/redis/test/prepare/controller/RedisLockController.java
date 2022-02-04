package org.smartframework.cloud.starter.redis.test.prepare.controller;

import org.smartframework.cloud.starter.redis.annotation.RedisLock;
import org.smartframework.cloud.starter.redis.test.prepare.vo.User;
import org.springframework.stereotype.Component;

@Component
public class RedisLockController {

    @RedisLock(expressions = {"#mobile", "#user.id"})
    public String test(User user, String mobile) {
        return "ok";
    }

}
