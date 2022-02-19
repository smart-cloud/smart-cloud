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

import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * 密码安全处理工具类
 *
 * @author collin
 * @date 2019-07-01
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * 密码md5加盐安全处理
     *
     * @param password 原始密码
     * @param salt     盐值
     * @return
     */
    public static String secure(String password, String salt) {
        // 第一次md5
        String md5 = DigestUtils.md5Hex(password + salt);
        // 截取前8位，继续第二次md5
        int sublength = 8;
        String subMd5 = md5.substring(sublength);
        md5 = DigestUtils.md5Hex(subMd5 + salt);
        return md5.substring(0, sublength) + md5.substring(sublength);
    }

    /**
     * 生成16位随机盐值
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String generateRandomSalt() throws NoSuchAlgorithmException {
        return SecureRandomUtil.generateRandom(false, 16);
    }

}