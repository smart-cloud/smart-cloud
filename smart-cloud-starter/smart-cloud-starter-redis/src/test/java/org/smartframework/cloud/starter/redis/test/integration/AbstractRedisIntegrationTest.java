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