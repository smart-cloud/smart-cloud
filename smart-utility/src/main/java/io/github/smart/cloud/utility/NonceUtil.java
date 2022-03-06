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
 * 全局id生成工具类
 *
 * @author collin
 * @date 2022-03-07
 */
public class NonceUtil {

    private static final SnowflakeId SNOW_FLAKE_ID = new SnowflakeId(null);

    private NonceUtil() {
    }

    public static long nextId() {
        return SNOW_FLAKE_ID.nextId();
    }

}