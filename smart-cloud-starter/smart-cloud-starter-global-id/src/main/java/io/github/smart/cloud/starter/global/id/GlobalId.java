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
package io.github.smart.cloud.starter.global.id;

import io.github.smart.cloud.utility.SnowflakeId;

/**
 * 全局id生成器
 *
 * @author collin
 * @date 2022-03-08
 */
public class GlobalId {

    private static SnowflakeId snowflakeId = null;

    /**
     * 初始化workerId
     *
     * @param workerId
     */
    public static void init(long workerId) {
        snowflakeId = new SnowflakeId(workerId);
    }

    public static long nextId() {
        if (snowflakeId == null) {
            throw new NullPointerException("GlobalId[workId] is not initedid!");
        }

        return snowflakeId.nextId();
    }

}