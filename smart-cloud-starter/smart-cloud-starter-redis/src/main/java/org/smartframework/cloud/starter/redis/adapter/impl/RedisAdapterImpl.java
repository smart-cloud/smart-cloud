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
package org.smartframework.cloud.starter.redis.adapter.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.smartframework.cloud.starter.redis.adapter.IRedisAdapter;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * redis常用api封装
 *
 * @author collin
 * @date 2018-10-17
 */
@RequiredArgsConstructor
public class RedisAdapterImpl implements IRedisAdapter {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 设置k-v对
     *
     * @param key
     * @param value
     * @param expireMillis 有效期（毫秒），为null表示不设置有效期
     */
    @Override
    public void setString(String key, String value, Long expireMillis) {
        if (expireMillis == null) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, expireMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    @Override
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置key有效期
     *
     * @param key
     * @param expireMillis 有效期（毫秒）
     */
    @Override
    public void expire(String key, Long expireMillis) {
        stringRedisTemplate.expire(key, expireMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean delete(Collection<String> keys) {
        Long count = stringRedisTemplate.delete(keys);
        return count != null && count == keys.size();
    }

    /**
     * 异步删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean unlink(String key) {
        return stringRedisTemplate.unlink(key);
    }

    /**
     * 异步批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean unlink(Collection<String> keys) {
        Long count = stringRedisTemplate.unlink(keys);
        return count != null && count == keys.size();
    }

    /**
     * 设置k-v对
     *
     * @param key
     * @param value        对象
     * @param expireMillis 有效期（毫秒），为null表示不设置有效期
     */
    @Override
    public void setObject(String key, Object value, Long expireMillis) {
        setString(key, JacksonUtil.toJson(value), expireMillis);
    }

    /**
     * 根据key获取Object
     *
     * @param key
     * @param t
     * @param <T> 返回对象类型
     * @return
     */
    @Override
    public <T> T getObject(String key, TypeReference<T> t) {
        String value = getString(key);
        if (null == value) {
            return null;
        }

        return JacksonUtil.parseObject(value, t);
    }

    /**
     * 不存在则设置；存在则不设置
     *
     * @param key
     * @param value
     * @param expireMillis 有效期（毫秒）
     * @return {@code true}表示成功；{@code false}表示失败
     */
    @Override
    public boolean setNx(String key, String value, long expireMillis) {
        Boolean result = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.set(key.getBytes(), value.getBytes(), Expiration.milliseconds(expireMillis),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
            }
        }, true);
        return result != null && result;
    }

}