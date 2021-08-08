package org.smartframework.cloud.utility.security;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.smartframework.cloud.utility.constant.SecurityConst;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES对称加密工具类
 *
 * @author liyulin
 * @date 2019-06-24
 */
@Slf4j
public class AesUtil {

    private AesUtil() {
    }

    /**
     * AES加密
     *
     * @param content  字符串内容
     * @param password 密钥
     */
    public static String encrypt(String content, String password) {
        return aes(content, password, Cipher.ENCRYPT_MODE);
    }

    /**
     * AES解密
     *
     * @param content  字符串内容
     * @param password 密钥
     */
    public static String decrypt(String content, String password) {
        return aes(content, password, Cipher.DECRYPT_MODE);
    }

    /**
     * AES加密/解密 公共方法
     *
     * @param content  字符串
     * @param password 密钥
     * @param type     类型：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     */
    private static String aes(String content, String password, int type) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(SecurityConst.ENCRYPTION_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(SecurityConst.RNG_ALGORITHM);
            random.setSeed(password.getBytes());
            generator.init(128, random);
            SecretKey secretKey = generator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, SecurityConst.ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(SecurityConst.ENCRYPTION_ALGORITHM);
            cipher.init(type, key);
            if (type == Cipher.ENCRYPT_MODE) {
                byte[] byteContent = content.getBytes(StandardCharsets.UTF_8.name());
                return ByteUtils.toHexString(cipher.doFinal(byteContent));
            } else {
                byte[] byteContent = ByteUtils.fromHexString(content);
                return new String(cipher.doFinal(byteContent));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}