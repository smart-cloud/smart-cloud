package org.smartframework.cloud.starter.common.business.util;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.smartframework.cloud.utility.SecureRandomUtil;

import lombok.experimental.UtilityClass;

/**
 * 密码安全处理工具类
 *
 * @author liyulin
 * @date 2019-07-01
 */
@UtilityClass
public class PasswordUtil {

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