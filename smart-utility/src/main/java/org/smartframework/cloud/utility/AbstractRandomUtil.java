package org.smartframework.cloud.utility;

import java.util.Random;

public abstract class AbstractRandomUtil {

	/** 随机字符表 */
	protected static String randomStrTable = null;
	/** 随机数字表 */
	protected static String randomPureNumberTable = null;
	static {
		StringBuilder strTableTmp = new StringBuilder();
		// 数字
		for (int i = 0; i <= 9; i++) {
			strTableTmp.append(i);
		}
		randomPureNumberTable = strTableTmp.toString();

		// 字母
		for (int i = 'a'; i <= 'z'; i++) {
			strTableTmp.append((char) i);
		}
		for (int i = 'A'; i <= 'Z'; i++) {
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