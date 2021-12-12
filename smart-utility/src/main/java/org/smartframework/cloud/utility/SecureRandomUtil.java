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
package org.smartframework.cloud.utility;

import org.smartframework.cloud.utility.constant.SecurityConst;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 安全的随机生成工具类
 *
 * @author collin
 * @date 2019-07-01
 */
public class SecureRandomUtil extends AbstractRandomUtil {

    private SecureRandomUtil() {
    }

    /**
     * 创建指定数量的随机字符串
     *
     * @param pureNumber 是否是纯数字
     * @param length     随机串长度
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String generateRandom(boolean pureNumber, int length) throws NoSuchAlgorithmException {
        return generateRandom(getSecureRandom(), pureNumber, length);
    }

    /**
     * 生成指定范围随机数
     *
     * @param min
     * @param max
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static int generateRangeRandom(int min, int max) throws NoSuchAlgorithmException {
        return generateRangeRandom(getSecureRandom(), min, max);
    }

    private static SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        return SecureRandom.getInstance(SecurityConst.RNG_ALGORITHM);
    }

}