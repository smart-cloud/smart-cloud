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
package io.github.smart.cloud.starter.mybatis.plus.util;

import io.github.smart.cloud.starter.configure.properties.MybatisProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.utility.security.AesUtil;
import io.github.smart.cloud.utility.spring.SpringContextUtil;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 表字段加解密工具类
 *
 * @author collin
 * @date 2022-02-06
 */
public final class FieldCryptUtil {

    private static Map<String, String> cryptKeys;

    private FieldCryptUtil() {
    }

    /**
     * AES加密
     *
     * @param content             待加密字符串
     * @param cryptFieldClassName
     */
    public static String encrypt(String content, String cryptFieldClassName) {
        return AesUtil.encrypt(content, getCryptKey(cryptFieldClassName));
    }

    /**
     * AES解密
     *
     * @param content             待解密字符串
     * @param cryptFieldClassName
     */
    public static String decrypt(String content, String cryptFieldClassName) {
        return AesUtil.decrypt(content, getCryptKey(cryptFieldClassName));
    }

    /**
     * 获取加解密密钥
     *
     * @return
     */
    private static String getCryptKey(String cryptFieldClassName) {
        if (cryptKeys == null) {
            synchronized (FieldCryptUtil.class) {
                if (cryptKeys == null) {
                    SmartProperties smartProperties = SpringContextUtil.getBean(SmartProperties.class);
                    MybatisProperties mybatisProperties = smartProperties.getMybatis();
                    Assert.notEmpty(mybatisProperties.getCryptKeys(), "cryptKeys not configured");
                    cryptKeys = mybatisProperties.getCryptKeys();
                }
            }
        }

        String cryptKey = cryptKeys.get(cryptFieldClassName);
        Assert.hasText(cryptKey, String.format("cryptKey[%S] not configured", cryptFieldClassName));
        return cryptKey;
    }

}