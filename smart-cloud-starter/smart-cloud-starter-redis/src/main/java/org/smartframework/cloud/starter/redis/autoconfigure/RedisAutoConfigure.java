package org.smartframework.cloud.starter.redis.autoconfigure;

import org.redisson.Redisson;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * redis配置
 *
 * @author liyulin
 * @date 2018-10-17
 */
@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@EnableCaching
public class RedisAutoConfigure {

    @Primary
    @Bean
    public RedisTemplate<Serializable, Serializable> initRedisTemplate(final RedisConnectionFactory connectionFactory) {
        RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate initStringRedisTemplate(final RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return stringRedisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        return new SimpleCacheManager();
    }

    @Bean
    public ValueOperations<Serializable, Serializable> redisValueOperations(
            final RedisTemplate<Serializable, Serializable> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<Serializable, Serializable> redisListOperations(
            final RedisTemplate<Serializable, Serializable> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public SetOperations<Serializable, Serializable> redisSetOperations(
            final RedisTemplate<Serializable, Serializable> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<Serializable, Serializable> redisZSetOperations(
            final RedisTemplate<Serializable, Serializable> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    @Bean
    public HashOperations<Serializable, Serializable, Serializable> redisHashOperations(
            final RedisTemplate<Serializable, Serializable> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public RedisComponent redisComponent(final StringRedisTemplate stringRedisTemplate) {
        return new RedisComponent(stringRedisTemplate);
    }

}