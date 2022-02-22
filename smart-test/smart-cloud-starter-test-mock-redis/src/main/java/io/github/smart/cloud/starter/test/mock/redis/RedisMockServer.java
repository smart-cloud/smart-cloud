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
package io.github.smart.cloud.starter.test.mock.redis;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

/**
 * redis server mock
 *
 * @author collin
 * @date 2022-02-22
 */
public class RedisMockServer {

    private RedisServer redisServer = null;
    /**
     * redis server端口
     */
    private static final int DEFAULT_PORT = 6379;

    /**
     * 启动redis
     *
     * @param port
     */
    public void start(String password, int port) {
        // https://github.com/kstyrc/embedded-redis/issues/51
        RedisServerBuilder redisServerBuilder = RedisServer.builder();
        redisServerBuilder.port(port != 0 ? port : DEFAULT_PORT);
        redisServerBuilder.setting("maxmemory 128M");
        if (password != null && password.trim().length() > 0) {
            redisServerBuilder.setting(String.format("requirepass %s", password));
        }
        redisServer = redisServerBuilder.build();

        redisServer.start();
    }

    /**
     * 停止Redis.
     */
    public void stop() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}