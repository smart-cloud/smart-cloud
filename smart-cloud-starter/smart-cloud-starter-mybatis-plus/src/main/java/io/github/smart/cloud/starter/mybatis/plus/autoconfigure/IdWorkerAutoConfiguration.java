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
package io.github.smart.cloud.starter.mybatis.plus.autoconfigure;

import io.github.smart.cloud.exception.AcquiredLockFailException;
import io.github.smart.cloud.starter.mybatis.plus.constants.RedisKey;
import io.github.smart.cloud.starter.mybatis.plus.util.IdWorker;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * {@link IdWorker}参数配置
 *
 * @author collin
 * @date 2022-03-06
 */
@Configuration
@RequiredArgsConstructor
public class IdWorkerAutoConfiguration implements InitializingBean {

    private final RedissonClient redissonClient;
    /**
     * 获取锁的最大等待时间（10秒）
     */
    private final long maxLockWaitSeconds = 10L;

    @Override
    public void afterPropertiesSet() throws Exception {
        RAtomicLong workIdAtomicLong = redissonClient.getAtomicLong(RedisKey.IDWORKER_WORKERID);
        RAtomicLong dataCenterIdAtomicLong = redissonClient.getAtomicLong(RedisKey.IDWORKER_DATACENTERID);

        long workId = workIdAtomicLong.get();
        long dataCenterId = 0L;
        try {
            dataCenterId = dataCenterIdAtomicLong.incrementAndGet();
        } catch (RedisException dataCenterIdIncrementException) {
            RLock lock = redissonClient.getLock(RedisKey.IDWORKER);
            try {
                if (!lock.tryLock(maxLockWaitSeconds, TimeUnit.SECONDS)) {
                    throw new AcquiredLockFailException();
                }

                dataCenterId = dataCenterIdAtomicLong.get();
                if (dataCenterId != Long.MAX_VALUE) {
                    dataCenterId = dataCenterIdAtomicLong.incrementAndGet();
                } else {
                    try {
                        workId = workIdAtomicLong.incrementAndGet();
                    } catch (RedisException workIdIncrementException) {
                        workId = 0L;
                        workIdAtomicLong.set(workId);
                    }
                    dataCenterId = 0L;
                    dataCenterIdAtomicLong.set(dataCenterId);
                }
            } finally {
                lock.unlock();
            }
        }
        workId = workId % 32;
        dataCenterId = dataCenterId % 32;
        IdWorker.getInstance().init(workId, dataCenterId);
    }

}