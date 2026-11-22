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
package io.github.smart.cloud.starter.logback.appender;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;

import java.util.regex.Pattern;

/**
 * 支持脱敏的异步appender
 *
 * <p>logback原生{@link AsyncAppender}在独立线程中执行下游appender，
 * 此时业务线程{@link LogMaskContext}（ThreadLocal）中的脱敏正则已不可见，注解脱敏会失效。
 * 本appender在业务线程入队前捕获上下文中的正则，包装为{@link MaskLoggingEvent}，
 * 日志线程中由{@link io.github.smart.cloud.starter.logback.pattern.MaskMessageConverter}（%mm）取出后再脱敏。
 *
 * <p>用法与{@link AsyncAppender}相同：
 * <pre>
 * &lt;appender name="async_console" class="io.github.smart.cloud.starter.logback.appender.MaskAsyncAppender"&gt;
 *     &lt;includeCallerData&gt;true&lt;/includeCallerData&gt;
 *     &lt;appender-ref ref="console"/&gt;
 * &lt;/appender&gt;
 * </pre>
 *
 * @author collin
 * @date 2026-11-22
 */
public class MaskAsyncAppender extends AsyncAppender {

    @Override
    protected void append(ILoggingEvent eventObject) {
        Pattern pattern = LogMaskContext.get();
        // 上下文中没有脱敏正则时，与日志线程中的上下文（同样为空）等价，无需包装
        if (pattern == null || eventObject instanceof MaskLoggingEvent) {
            super.append(eventObject);
            return;
        }
        super.append(new MaskLoggingEvent(eventObject, pattern));
    }

}
