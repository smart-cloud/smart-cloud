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
import io.github.smart.cloud.mask.util.MaskUtil;
import org.slf4j.helpers.MessageFormatter;

/**
 * logback日志脱敏
 *
 * @author collin
 * @date 2022-12-02
 */
public class MaskMessageConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] = MaskUtil.mask(args[i]);
            }

            return MessageFormatter.arrayFormat(event.getMessage(), args).getMessage();
        }

        return event.getFormattedMessage();
    }

}