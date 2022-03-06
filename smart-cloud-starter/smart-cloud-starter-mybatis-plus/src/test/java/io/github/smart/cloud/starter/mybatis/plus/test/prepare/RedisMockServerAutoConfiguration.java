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
package io.github.smart.cloud.starter.mybatis.plus.test.prepare;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class RedisMockServerAutoConfiguration implements EnvironmentAware, DisposableBean {

    /**
     * 启动redis server
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        if (RedisMockServer.isActive()) {
            log.warn("redis server is already running");
            return;
        }
        
        String password = environment.getProperty("spring.redis.password");
        Integer port = environment.getProperty("spring.redis.port", Integer.class, 0);
        RedisMockServer.start(password, port);
    }

    /**
     * redis server停止
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        RedisMockServer.stop();
    }

}