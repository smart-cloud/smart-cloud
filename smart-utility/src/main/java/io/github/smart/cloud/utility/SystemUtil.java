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
package io.github.smart.cloud.utility;

/**
 * 系统工具类
 *
 * @author collin
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