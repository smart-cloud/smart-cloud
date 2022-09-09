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

import io.github.smart.cloud.utility.RandomUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RandomUtilUnitTest {

    @Test
    void testGenerateRandom() {
        String random1 = RandomUtil.generateRandom(true, 10);
        String random2 = RandomUtil.generateRandom(false, 10);
        Assertions.assertThat(random1).hasSize(10);
        Assertions.assertThat(random2).hasSize(10);
    }

    @Test
    void testUuid() {
        Assertions.assertThat(RandomUtil.uuid()).isNotBlank();
    }

    @Test
    void testGenerateRangeRandom() {
        int random1 = RandomUtil.generateRangeRandom(10, 100);
        Assertions.assertThat(random1).isBetween(10, 100);
    }

    @Test
    void testRandomSortArray() {
        Integer[] data = {1, 3, 5, 7, 8, 9, 2, 3, 5};
        RandomUtil.randomSort(data);

        Assertions.assertThat(RandomUtil.randomSort(new Integer[]{})).isEmpty();
    }

    @Test
    void testRandomSortList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(123);
        list.add(2);
        list.add(23);
        list.add(3);
        RandomUtil.randomSort(list);

        Assertions.assertThat(RandomUtil.randomSort(Collections.emptyList())).isEmpty();
    }

}