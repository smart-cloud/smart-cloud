package org.smartframework.cloud.utility;

import lombok.experimental.UtilityClass;

/**
 * 系统工具类
 *
 * @author liyulin
 * @date 2019-04-04
 */
@UtilityClass
public class SystemUtil {

	public static final String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * 是否是windows
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		return OS.indexOf("windows") >= 0;
	}

	/**
	 * 是否是linux
	 * 
	 * @return
	 */
	public static boolean isLinux() {
		return OS.indexOf("linux") >= 0;
	}

	/**
	 * 获取用户目录
	 * 
	 * @return
	 */
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

}