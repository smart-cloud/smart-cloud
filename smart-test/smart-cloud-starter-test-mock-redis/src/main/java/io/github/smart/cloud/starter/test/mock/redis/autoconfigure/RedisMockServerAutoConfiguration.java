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
package io.github.smart.cloud.starter.test.mock.redis.autoconfigure;

import io.github.smart.cloud.starter.test.mock.redis.constants.RedisPropertiesKey;
import io.github.smart.cloud.starter.test.mock.redis.RedisMockServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * redis mock server配置
 *
 * @author collin
 * @date 2022-02-22
 */
@Configuration
public class RedisMockServerAutoConfiguration implements EnvironmentAware, DisposableBean {

    private RedisMockServer redisMockServer;

    /**
     * 启动redis server
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        redisMockServer = new RedisMockServer();
        String password = environment.getProperty(RedisPropertiesKey.PASSWORD);
        Integer port = environment.getProperty(RedisPropertiesKey.PORT, Integer.class, 0);
        redisMockServer.start(password, port);
    }

    /**
     * redis server停止
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        if (redisMockServer != null) {
            redisMockServer.stop();
        }
    }

}