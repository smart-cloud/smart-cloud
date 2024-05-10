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
package io.github.smart.cloud.starter.redis.adapter.impl;

import io.github.smart.cloud.starter.redis.adapter.IRedisAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis常用api封装
 *
 * @author collin
 * @date 2018-10-17
 */
@Slf4j
@RequiredArgsConstructor
public class RedisAdapterImpl implements IRedisAdapter {

    private final RedisTemplate<Object, Object> redisTemplate;


    /**
     * 获取匹配的key
     *
     * @param pattern
     * @return
     */
    @Override
    public Set<Object> keys(Object pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 设置k-v对
     *
     * @param key
     * @param value
     * @param expireMillis 有效期（毫秒），为null表示不设置有效期
     */
    @Override
    public void set(Object key, Object value, Long expireMillis) {
        if (expireMillis == null) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, expireMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置key有效期
     *
     * @param key
     * @param expireMillis 有效期（毫秒）
     */
    @Override
    public void expire(Object key, Long expireMillis) {
        redisTemplate.expire(key, expireMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean delete(Object key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean delete(Collection<Object> keys) {
        Long count = redisTemplate.delete(keys);
        return count != null && count == keys.size();
    }

    /**
     * 异步删除k-v对
     *
     * @param key
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean unlink(Object key) {
        return redisTemplate.unlink(key);
    }

    /**
     * 异步批量删除k-v对
     *
     * @param keys
     * @return {@code true}表示成功；{@code false}表示失败。删除一个不存在的key，将返回{@code false}！！！
     */
    @Override
    public Boolean unlink(Collection<Object> keys) {
        Long count = redisTemplate.unlink(keys);
        return count != null && count == keys.size();
    }

    /**
     * 获取缓存有效期
     *
     * @param key
     * @param timeUnit
     * @return
     */
    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
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
        Boolean result = redisTemplate.execute(connection -> connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8), Expiration.milliseconds(expireMillis), RedisStringCommands.SetOption.SET_IF_ABSENT), true);
        return result != null && result;
    }

    /**
     * 设置hash结构缓存，并设置有效期
     *
     * @param key
     * @param data
     * @param expireSeconds
     * @return
     */
    @Override
    public boolean setHash(String key, Map<String, Object> data, Integer expireSeconds) {
        Assert.hasText(key, "The arg[key] can not be empty");
        Assert.notEmpty(data, "The arg[data] can not be empty");
        Assert.notNull(expireSeconds, "The arg[expireSeconds] can not be null");

        List<Object> keys = new ArrayList<>();
        keys.add(key);

        List<Object> args = new ArrayList<>();
        args.add(expireSeconds);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            keys.add(entry.getKey());
            args.add(entry.getValue());
        }

        String scriptText = buildHashLuaScript(data);
        log.debug(scriptText);

        DefaultRedisScript<Long> mapRedisScript = new DefaultRedisScript<>();
        mapRedisScript.setScriptText(scriptText);
        mapRedisScript.setResultType(Long.TYPE);

        Long result = redisTemplate.execute(mapRedisScript, keys, args.toArray());
        return result != null && result == 1L;
    }

    /**
     * 根据key获取hash
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hash值
     *
     * @param key
     * @param hashKey
     * @param <T>
     * @return
     */
    @Override
    public <T> T get(String key, Object hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 构建hash结构lua脚本
     *
     * @param data
     * @return
     */
    private final String buildHashLuaScript(Map<String, Object> data) {
        StringBuilder hashLua = new StringBuilder(64);
        hashLua.append("redis.call('hmset', KEYS[1], ");

        for (int index = 0, size = data.size(); index < size; index++) {
            int luaArgIndex = index + 2;
            hashLua.append("KEYS[").append(luaArgIndex).append("], ARGV[").append(luaArgIndex).append(']');
            if (index < size - 1) {
                hashLua.append(',');
            }
        }

        hashLua.append("); return redis.call('expire', KEYS[1], ARGV[1]);");

        return hashLua.toString();
    }

}