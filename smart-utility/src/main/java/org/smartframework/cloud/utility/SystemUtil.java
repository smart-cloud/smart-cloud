package org.smartframework.cloud.utility;

/**
 * 系统工具类
 *
 * @author liyulin
 * @date 2019-04-04
 */
public class SystemUtil {


    private SystemUtil() {
    }

    /**
     * 是否是windows
     *
     * @return
     */
    public static boolean isWindows() {
        return Holder.OS.indexOf("windows") >= 0;
    }

    /**
     * 是否是linux
     *
     * @return
     */
    public static boolean isLinux() {
        return Holder.OS.indexOf("linux") >= 0;
    }

    /**
     * 获取用户目录
     *
     * @return
     */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    static class Holder {
        private static final String OS = System.getProperty("os.name").toLowerCase();
    }

}