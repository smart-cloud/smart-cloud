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
package org.smartframework.cloud.utility;

/**
 * Object工具类
 *
 * @author liyulin
 * @date 2019-04-07
 */
public class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 是否为null
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 是否不为null
     *
     * @param object
     * @return
     */
    public static boolean isNotNull(Object object) {
        return object != null;
    }

    /**
     * objects是否都为null
     *
     * @param objects
     * @return
     */
    public static boolean isAllNull(Object... objects) {
        if (objects == null) {
            return true;
        }

        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * objects是否都不为null
     *
     * @param objects
     * @return
     */
    public static boolean isAllNotNull(Object... objects) {
        if (objects == null) {
            return false;
        }

        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两对象的字符串是否相等
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

}