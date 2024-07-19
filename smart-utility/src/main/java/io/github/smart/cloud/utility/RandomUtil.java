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
package io.github.smart.cloud.utility;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * @author collin
 * @date 2019-04-08
 */
public class RandomUtil extends AbstractRandomUtil {

    private RandomUtil() {
        super();
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param pureNumber 是否是纯数字
     * @param length     随机串长度
     * @return
     */
    public static String generateRandom(boolean pureNumber, int length) {
        return generateRandom(ThreadLocalRandom.current(), pureNumber, length);
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成指定范围随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int generateRangeRandom(int min, int max) {
        return generateRangeRandom(ThreadLocalRandom.current(), min, max);
    }

    /**
     * 随机排序
     *
     * @param array
     * @return
     */
    public static <E> E[] randomSort(E[] array) {
        if (array == null || array.length == 0) {
            return array;
        }

        for (int i = 0, len = array.length; i < len; i++) {
            int currentRandom = ThreadLocalRandom.current().nextInt(len);
            E current = array[i];
            array[i] = array[currentRandom];
            array[currentRandom] = current;
        }

        return array;
    }

    /**
     * 随机排序
     *
     * @param list
     * @return
     */
    public static <E> List<E> randomSort(List<E> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        for (int i = 0, len = list.size(); i < len; i++) {
            int currentRandom = ThreadLocalRandom.current().nextInt(len);
            E current = list.get(i);
            list.set(i, list.get(currentRandom));
            list.set(currentRandom, current);
        }

        return list;
    }

}