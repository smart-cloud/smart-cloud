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
package io.github.smart.cloud.starter.global.id.test.cases;

import io.github.smart.cloud.starter.global.id.GlobalId;
import io.github.smart.cloud.starter.global.id.constants.RedisKey;
import io.github.smart.cloud.starter.global.id.test.prepare.GlobalIdApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(GlobalIdTest.GlobalIdValueAutoConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GlobalIdApp.class, args = "--spring.profiles.active=globalid")
public class GlobalIdTest extends AbstractIntegrationTest {

    @Test
    void test() {
        GlobalId.nextId();
    }

    public static class GlobalIdValueAutoConfiguration implements ApplicationContextAware {

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            RedissonClient redissonClient = applicationContext.getBean(RedissonClient.class);
            RAtomicLong workIdAtomicLong = redissonClient.getAtomicLong(RedisKey.GLOBALID_WORKERID);
            workIdAtomicLong.set(Long.MAX_VALUE);
        }
        
    }

}