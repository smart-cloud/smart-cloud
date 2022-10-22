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
package io.github.smart.cloud.starter.redis.adapter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * redis防腐层
 *
 * @author collin
 * @date 2021-12-15
 */
public interface IRedisAdapter {

    /**
     * 获取匹配的key
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 设置k-v对
     *
     * @param key
     * @param value
     * @param expireMillis 有效期（毫秒），为null表示不设置有效期
     */
    void setString(String key, String value, Long expireMillis);

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 设置key有效期
     *
     * @param key
     * @param expireMillis 有效期（毫秒）
     */
    void expire(String key, Long expireMillis);

    /**
     * 删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    Boolean delete(String key);

    /**
     * 批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    Boolean delete(Collection<String> keys);

    /**
     * 异步删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    Boolean unlink(String key);

    /**
     * 异步批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    Boolean unlink(Collection<String> keys);

    /**
     * 设置k-v对
     *
     * @param key
     * @param value        对象
     * @param expireMillis 有效期（毫秒），为null表示不设置有效期
     */
    void setObject(String key, Object value, Long expireMillis);

    /**
     * 根据key获取Object
     *
     * @param key
     * @param <T> 返回对象类型
     * @return
     */
    <T> T getObject(String key);

    /**
     * 不存在则设置；存在则不设置
     *
     * @param key
     * @param value
     * @param expireMillis 有效期（毫秒）
     * @return {@code true}表示成功；{@code false}表示失败
     */
    boolean setNx(String key, String value, long expireMillis);

    /**
     * 设置hash结构缓存，并设置有效期
     *
     * @param key
     * @param data
     * @param expireSeconds
     * @return
     */
    boolean setHash(String key, Map<String, Object> data, Integer expireSeconds);

    /**
     * 获取hash值
     *
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    <T> T get(String key, Object hashKey);

}