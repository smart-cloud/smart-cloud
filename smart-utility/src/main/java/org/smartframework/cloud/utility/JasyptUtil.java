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

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.springframework.core.env.StandardEnvironment;

/**
 * jasypt加解密工具类
 *
 * @author liyulin
 * @date 2019-05-24
 */
public class JasyptUtil {

    private static StandardEnvironment environment = new StandardEnvironment();
    private static DefaultLazyEncryptor encryptor = new DefaultLazyEncryptor(environment);

    private JasyptUtil() {
    }

    /**
     * 加密
     *
     * @param salt
     * @param message
     * @return
     */
    public static String encryptor(String salt, String message) {
        environment.getSystemProperties().put("jasypt.encryptor.password", salt);
        return encryptor.encrypt(message);
    }

    /**
     * 解密
     *
     * @param salt
     * @param message
     * @return
     */
    public static String decrypt(String salt, String message) {
        environment.getSystemProperties().put("jasypt.encryptor.password", salt);
        return encryptor.decrypt(message);
    }

}