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

import java.util.Random;

/**
 * 随机工具父类
 *
 * @author collin
 * @date 2021-10-31
 */
public abstract class AbstractRandomUtil {

    /**
     * 随机字符表
     */
    protected static String randomStrTable;
    /**
     * 随机数字表
     */
    protected static String randomPureNumberTable;

    static {
        StringBuilder strTableTmp = new StringBuilder();
        // 数字
        int numStart = 0;
        int numEnd = 9;
        for (int i = numStart; i <= numEnd; i++) {
            strTableTmp.append(i);
        }
        randomPureNumberTable = strTableTmp.toString();

        // 字母
        char lowerLetterStart = 'a';
        char lowerLetterEnd = 'z';
        for (int i = lowerLetterStart; i <= lowerLetterEnd; i++) {
            strTableTmp.append((char) i);
        }

        char upperLetterStart = 'A';
        char upperLetterEnd = 'Z';
        for (int i = upperLetterStart; i <= upperLetterEnd; i++) {
            strTableTmp.append((char) i);
        }
        randomStrTable = strTableTmp.toString();
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param random
     * @param pureNumber 是否是纯数字
     * @param length     随机串长度
     * @return
     */
    protected static String generateRandom(Random random, boolean pureNumber, int length) {
        StringBuilder result = new StringBuilder(length);
        String strTable = pureNumber ? randomPureNumberTable : randomStrTable;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(strTable.length());
            result.append(strTable.charAt(index));
        }

        return result.toString();
    }

    /**
     * 生成指定范围随机数
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    protected static int generateRangeRandom(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

}