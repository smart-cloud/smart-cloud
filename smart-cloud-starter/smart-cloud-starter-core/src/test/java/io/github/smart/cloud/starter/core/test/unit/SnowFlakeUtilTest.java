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
package io.github.smart.cloud.starter.core.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.smart.cloud.starter.core.business.util.SnowFlakeIdUtil;
import io.github.smart.cloud.starter.core.test.unit.prepare.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class SnowFlakeUtilTest {

    @RepeatedTest(128)
    void testnextId() throws InterruptedException {
        int parties = 128;
        CountDownLatch latch = new CountDownLatch(parties);
        CopyOnWriteArraySet<String> values = new CopyOnWriteArraySet<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(parties);
        for (int i = 0; i < parties; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    values.add(String.valueOf(SnowFlakeIdUtil.getInstance().nextId()));
                    latch.countDown();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        latch.await();
        Assertions.assertThat(values).hasSize(parties);
    }

}