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
package io.github.smart.cloud.utility.test.unit;

import io.github.smart.cloud.utility.concurrent.EagerThreadPoolExecutor;
import io.github.smart.cloud.utility.concurrent.EagerThreadPoolTaskQueue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class EagerThreadPoolExecutorUnitTest {

    @Test
    void test() {
        int corePoolSize = 2;
        int maxPoolSize = 10;
        int queueCapacity = 20;
        EagerThreadPoolTaskQueue<Runnable> eagerThreadPoolTaskQueue = new EagerThreadPoolTaskQueue<>(queueCapacity);
        EagerThreadPoolExecutor eagerThreadPoolExecutor = new EagerThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS,
                eagerThreadPoolTaskQueue, "unit-test", new ThreadPoolExecutor.CallerRunsPolicy());

        int taskSize = 25;
        for (int i = 0; i < taskSize; i++) {
            eagerThreadPoolExecutor.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                }
            });
        }

        try {
            TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {
        }

        Assertions.assertThat(eagerThreadPoolExecutor.getCompletedTaskCount()).isEqualTo(0);

        Assertions.assertThat(eagerThreadPoolExecutor.getActiveCount()).isEqualTo(maxPoolSize);
        Assertions.assertThat(eagerThreadPoolExecutor.getQueue().size()).isEqualTo(taskSize - maxPoolSize);

        Assertions.assertThat(eagerThreadPoolExecutor.getCompletedTaskCount()).isEqualTo(0);
    }

}