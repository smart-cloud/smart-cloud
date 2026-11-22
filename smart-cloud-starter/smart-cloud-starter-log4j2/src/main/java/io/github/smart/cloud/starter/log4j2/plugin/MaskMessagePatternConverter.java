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
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskUtil;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.util.regex.Pattern;

/**
 * 自定义log4j2模式匹配符
 *
 * @author collin
 * @date 2021-10-30
 */
@Plugin(name = "MaskMessagePatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"mm"})
@PerformanceSensitive("allocation")
public class MaskMessagePatternConverter extends LogEventPatternConverter {

    private MaskMessagePatternConverter() {
        super("Mmessage", "mmessage");
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param config  The Configuration.
     * @param options options, may be null.
     * @return instance of pattern converter.
     */
    public static MaskMessagePatternConverter newInstance(final Configuration config, final String[] options) {
        return new MaskMessagePatternConverter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        Message message = event.getMessage();
        String maskText = LogMaskUtil.mask(message.getFormattedMessage(), getMaskPattern(event));
        toAppendTo.append(maskText);
    }

    /**
     * 获取脱敏正则：优先取业务线程中随日志事件传递的正则（见{@link LogMaskContextDataProvider}），
     * 以支持异步打印；事件中没有时，取当前线程上下文中的正则（同步打印时与业务线程相同）。
     *
     * @param event
     * @return
     */
    private static Pattern getMaskPattern(final LogEvent event) {
        ReadOnlyStringMap contextData = event.getContextData();
        if (contextData != null) {
            String patternId = contextData.getValue(LogMaskContextDataProvider.MASK_PATTERN_ID_KEY);
            if (patternId != null) {
                Pattern pattern = LogMaskContextDataProvider.getPattern(patternId);
                if (pattern != null) {
                    return pattern;
                }
            }
        }
        return LogMaskContext.get();
    }

}