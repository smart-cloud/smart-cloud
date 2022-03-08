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
package io.github.smart.cloud.starter.global.id.autoconfigure;

import io.github.smart.cloud.exception.AcquiredLockFailException;
import io.github.smart.cloud.starter.global.id.GlobalId;
import io.github.smart.cloud.starter.global.id.constants.RedisKey;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * {@link GlobalId}参数配置
 *
 * @author collin
 * @date 2022-03-06
 */
@Configuration
@RequiredArgsConstructor
public class GlobalIdAutoConfiguration implements InitializingBean {

    private final RedissonClient redissonClient;
    /**
     * 获取锁的最大等待时间（10秒）
     */
    private final long maxLockWaitSeconds = 10L;
    /**
     * 雪花算法workId最大的值
     */
    private final long maxWorkerId = 1024;

    @Override
    public void afterPropertiesSet() throws Exception {
        RAtomicLong workIdAtomicLong = redissonClient.getAtomicLong(RedisKey.GLOBALID_WORKERID);

        long workId = 0L;
        try {
            workId = workIdAtomicLong.incrementAndGet();
        }
        // 当计数器递增至Long.MAX_VALUE时，会抛异常；此时从上一次的下一个余数开始继续计数
        catch (RedisException dataCenterIdIncrementException) {
            RLock lock = redissonClient.getLock(RedisKey.GLOBALID);
            boolean isRequiredLock = false;
            try {
                isRequiredLock = lock.tryLock(maxLockWaitSeconds, TimeUnit.SECONDS);
                if (!isRequiredLock) {
                    throw new AcquiredLockFailException();
                }

                workId = workIdAtomicLong.get();
                if (workId != Long.MAX_VALUE) {
                    workId = workIdAtomicLong.incrementAndGet();
                } else {
                    long currentStartId = (Long.MAX_VALUE % maxWorkerId) + 1;
                    workId = currentStartId;
                    workIdAtomicLong.set(workId);
                }
            } finally {
                if (isRequiredLock) {
                    lock.unlock();
                }
            }
        }

        workId = workId % maxWorkerId;
        GlobalId.init(workId);
    }

}