package io.github.smart.cloud.starter.actuator.util;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 先进先出缓存
 *
 * @param <K>
 * @param <V>
 * @author collin
 * @date 2024-01-16
 */
@Slf4j
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private final int maxElements;

    public LruCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        boolean needRemove = size() > maxElements;
        if (needRemove) {
            log.warn("LruCache invoke removeEldestEntry");
        }

        return needRemove;
    }

}