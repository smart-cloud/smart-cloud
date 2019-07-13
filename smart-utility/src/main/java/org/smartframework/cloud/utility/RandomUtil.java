package org.smartframework.cloud.utility;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import lombok.experimental.UtilityClass;

/**
 * 随机工具类
 *
 * @author liyulin
 * @date 2019-04-08
 */
@UtilityClass
public class RandomUtil extends AbstractRandomUtil{

	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param pureNumber 是否是纯数字
	 * @param length     随机串长度
	 * @return
	 */
	public static String generateRandom(boolean pureNumber, int length) {
		return generateRandom(ThreadLocalRandom.current(), pureNumber, length);
	}

	/**
	 * 生成uuid
	 * 
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 生成指定范围随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int generateRangeRandom(int min, int max) {
		return generateRangeRandom(ThreadLocalRandom.current(), min, max);
	}

	/**
	 * 随机排序
	 * 
	 * @param array
	 * @return
	 */
	public static <E> E[] randomSort(E[] array) {
		if (ArrayUtil.isEmpty(array)) {
			return array;
		}

		for (int i = 0, len = array.length; i < len; i++) {
			int currentRandom = ThreadLocalRandom.current().nextInt(len);
			E current = array[i];
			array[i] = array[currentRandom];
			array[currentRandom] = current;
		}

		return array;
	}

	/**
	 * 随机排序
	 * 
	 * @param list
	 * @return
	 */
	public static <E> List<E> randomSort(List<E> list) {
		if (CollectionUtil.isEmpty(list)) {
			return list;
		}

		for (int i = 0, len = list.size(); i < len; i++) {
			int currentRandom = ThreadLocalRandom.current().nextInt(len);
			E current = list.get(i);
			list.set(i, list.get(currentRandom));
			list.set(currentRandom, current);
		}

		return list;
	}

}