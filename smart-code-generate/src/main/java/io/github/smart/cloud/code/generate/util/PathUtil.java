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
package io.github.smart.cloud.code.generate.util;

/**
 * 路径工具类
 *
 * @author collin
 * @date 2021-12-12
 */
public class PathUtil {

    private PathUtil() {
    }

    /**
     * 获取rpc默认路径
     *
     * @return
     */
    public static String getDefaultRpcDir() {
        if (isWindowSystem()) {
            return "c:/codegenerate/rpc-module/";
        }
        return "/tmp/codegenerate/rpc-module/";
    }

    /**
     * 获取服务默认路径
     *
     * @return
     */
    public static String getDefaultServiceDir() {
        if (isWindowSystem()) {
            return "c:/codegenerate/service-module/";
        }
        return "/tmp/codegenerate/service-module/";
    }

    /**
     * 是否是windows系统
     *
     * @return
     */
    private static boolean isWindowSystem() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0;
    }

}