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
package io.github.smart.cloud.starter.log4j2.plugin;

import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import org.apache.logging.log4j.core.util.ContextDataProvider;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

/**
 * 日志脱敏上下文传递（支持异步打印日志脱敏）
 *
 * <p>log4j2在业务线程中创建日志事件时，会调用通过SPI（META-INF/services）注册的{@link ContextDataProvider}，
 * 并将结果保存到日志事件的contextData中；异步打印时（AsyncAppender、AsyncLogger），contextData随事件一起传递到日志线程。
 * 本类在业务线程中捕获{@link LogMaskContext}（ThreadLocal）中的脱敏正则，以id的形式放入contextData，
 * 日志线程中由{@link MaskMessagePatternConverter}（%mm）根据id取出正则后再脱敏。
 *
 * <p>NOTE：contextData只支持字符串，因此正则以id登记在本类中。正则通常来自
 * {@link io.github.smart.cloud.starter.log.mask.pattern.PatternConfig#convert(String)}（已缓存），数量有限。
 *
 * @author collin
 * @date 2026-11-22
 */
public class LogMaskContextDataProvider implements ContextDataProvider {

    /**
     * contextData中脱敏正则id的key
     */
    public static final String MASK_PATTERN_ID_KEY = "__log_mask_pattern_id";
    /**
     * 正则 -> id（Pattern未重写equals/hashCode，按对象实例区分）
     */
    private static final ConcurrentMap<Pattern, String> PATTERN_IDS = new ConcurrentHashMap<>();
    /**
     * id -> 正则
     */
    private static final ConcurrentMap<String, Pattern> PATTERNS = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @Override
    public Map<String, String> supplyContextData() {
        Pattern pattern = LogMaskContext.get();
        if (pattern == null) {
            return Collections.emptyMap();
        }
        return Collections.singletonMap(MASK_PATTERN_ID_KEY, register(pattern));
    }

    /**
     * 根据id获取脱敏正则
     *
     * @param id
     * @return
     */
    public static Pattern getPattern(String id) {
        return PATTERNS.get(id);
    }

    /**
     * 登记正则，返回其id
     *
     * @param pattern
     * @return
     */
    private static String register(Pattern pattern) {
        String id = PATTERN_IDS.get(pattern);
        if (id != null) {
            return id;
        }
        return PATTERN_IDS.computeIfAbsent(pattern, key -> {
            String newId = String.valueOf(ID_GENERATOR.incrementAndGet());
            PATTERNS.put(newId, key);
            return newId;
        });
    }

}
