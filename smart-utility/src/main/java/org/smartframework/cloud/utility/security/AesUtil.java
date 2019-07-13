package org.smartframework.cloud.utility.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * AES对称加密工具类
 * 
 * @author liyulin
 * @date 2019-06-24
 */
@UtilityClass
@Slf4j
public class AesUtil {
	
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
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			generator.init(128, random);
			SecretKey secretKey = generator.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
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