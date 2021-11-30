package org.smartframework.cloud.starter.redis.test.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.redis.test.prepare.App;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
public abstract class AbstractRedisIntegrationTest {

    private static RedisServer redisServer = null;
    /**
     * redis server端口
     */
    private static final int REDIS_SERVER_PORT = 6379;

    /**
     * 启动Redis，并在6379端口监听
     */
    @BeforeAll
    static void startRedis() {
        // https://github.com/kstyrc/embedded-redis/issues/51
        redisServer = RedisServer.builder()
                .port(REDIS_SERVER_PORT)
                //maxheap 128M
                .setting("maxmemory 128M")
                .build();

        redisServer.start();
    }

    /**
     * 析构方法之后执行，停止Redis.
     */
    @AfterAll
    static void stopRedis() {
        redisServer.stop();
    }

}