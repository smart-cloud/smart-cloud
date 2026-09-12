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
package io.github.smart.cloud.starter.redis.constants;

/**
 * Redis 中用于表示空缓存结果的占位值。
 *
 * @author collin
 * @date 2026-09-12
 */
public class NullCacheValue {

    /**
     * 空缓存结果占位值
     */
    public static final NullCacheValue INSTANCE = new NullCacheValue();

    private String marker = "NULL";

    /**
     * 默认构造方法，供 Redis 序列化器反序列化使用。
     */
    public NullCacheValue() {
    }

    public String getMarker() {
        return marker;
    }

}
