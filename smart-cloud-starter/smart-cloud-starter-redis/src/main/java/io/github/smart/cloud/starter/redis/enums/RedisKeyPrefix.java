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
package io.github.smart.cloud.starter.redis.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis key前缀
 *
 * @author collin
 * @date 2020-04-15
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RedisKeyPrefix {

    /**
     * redis key分隔符
     */
    REDIS_KEY_SEPARATOR(":"),
    /**
     * 用户缓存
     */
    CACHE("cache" + REDIS_KEY_SEPARATOR.key),
    /**
     * 用于加锁:
     */
    LOCK("lock" + REDIS_KEY_SEPARATOR.key),
    /**
     * 用于缓存加锁
     */
    LOCK_CACHE("lock" + REDIS_KEY_SEPARATOR.key + "cache" + REDIS_KEY_SEPARATOR.key),
    /**
     * 用户数据
     */
    DATA("data" + REDIS_KEY_SEPARATOR.key);

    private String key;

}