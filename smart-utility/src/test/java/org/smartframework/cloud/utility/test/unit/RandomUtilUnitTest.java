package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.RandomUtil;

import java.util.ArrayList;
import java.util.List;

class RandomUtilUnitTest {

    @Test
    void testGenerateRandom() {
        String random1 = RandomUtil.generateRandom(true, 10);
        String random2 = RandomUtil.generateRandom(false, 10);
        Assertions.assertThat(random1.length()).isEqualTo(10);
        Assertions.assertThat(random2.length()).isEqualTo(10);
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
    }

}