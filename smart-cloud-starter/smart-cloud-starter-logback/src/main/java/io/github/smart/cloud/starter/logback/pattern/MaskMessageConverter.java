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
package io.github.smart.cloud.starter.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskUtil;
import io.github.smart.cloud.starter.logback.appender.MaskLoggingEvent;

/**
 * logback日志脱敏
 *
 * <p>同步打印时取当前线程上下文中的脱敏正则；经{@link io.github.smart.cloud.starter.logback.appender.MaskAsyncAppender}
 * 异步打印时，取业务线程中捕获并随事件传递的脱敏正则。
 *
 * @author collin
 * @date 2022-12-02
 */
public class MaskMessageConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        if (event instanceof MaskLoggingEvent) {
            return LogMaskUtil.mask(event.getFormattedMessage(), ((MaskLoggingEvent) event).getMaskPattern());
        }
        return LogMaskUtil.mask(event.getFormattedMessage(), LogMaskContext.get());
    }

}