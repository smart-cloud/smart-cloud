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