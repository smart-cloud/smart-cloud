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