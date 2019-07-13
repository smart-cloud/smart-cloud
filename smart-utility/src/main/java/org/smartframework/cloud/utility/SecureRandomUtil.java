package org.smartframework.cloud.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.experimental.UtilityClass;

/**
 * 安全的随机生成工具类
 * 
 * @author liyulin
 * @date 2019-07-01
 */
@UtilityClass
public class SecureRandomUtil extends AbstractRandomUtil {

	/** 随机算法名称 */
	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

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
		return SecureRandom.getInstance(RANDOM_ALGORITHM);
	}

}