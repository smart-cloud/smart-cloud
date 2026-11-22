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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import org.slf4j.Marker;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 携带脱敏上下文的日志事件
 *
 * <p>在业务线程中捕获{@link io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext}中的正则，
 * 随日志事件一起进入异步队列，供日志线程脱敏时使用。其余方法全部委托给原始事件。
 *
 * @author collin
 * @date 2026-11-22
 */
public class MaskLoggingEvent implements ILoggingEvent {

    private final ILoggingEvent delegate;
    /**
     * 业务线程上下文中的脱敏正则
     */
    private final Pattern maskPattern;

    public MaskLoggingEvent(ILoggingEvent delegate, Pattern maskPattern) {
        this.delegate = delegate;
        this.maskPattern = maskPattern;
    }

    public Pattern getMaskPattern() {
        return maskPattern;
    }

    public ILoggingEvent getDelegate() {
        return delegate;
    }

    @Override
    public String getThreadName() {
        return delegate.getThreadName();
    }

    @Override
    public Level getLevel() {
        return delegate.getLevel();
    }

    @Override
    public String getMessage() {
        return delegate.getMessage();
    }

    @Override
    public Object[] getArgumentArray() {
        return delegate.getArgumentArray();
    }

    @Override
    public String getFormattedMessage() {
        return delegate.getFormattedMessage();
    }

    @Override
    public String getLoggerName() {
        return delegate.getLoggerName();
    }

    @Override
    public LoggerContextVO getLoggerContextVO() {
        return delegate.getLoggerContextVO();
    }

    @Override
    public IThrowableProxy getThrowableProxy() {
        return delegate.getThrowableProxy();
    }

    @Override
    public StackTraceElement[] getCallerData() {
        return delegate.getCallerData();
    }

    @Override
    public boolean hasCallerData() {
        return delegate.hasCallerData();
    }

    @Override
    public Marker getMarker() {
        return delegate.getMarker();
    }

    @Override
    public Map<String, String> getMDCPropertyMap() {
        return delegate.getMDCPropertyMap();
    }

    /**
     * @deprecated 同{@link ILoggingEvent#getMdc()}，请使用{@link #getMDCPropertyMap()}
     */
    @Override
    @Deprecated
    public Map<String, String> getMdc() {
        return delegate.getMdc();
    }

    @Override
    public long getTimeStamp() {
        return delegate.getTimeStamp();
    }

    @Override
    public void prepareForDeferredProcessing() {
        delegate.prepareForDeferredProcessing();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
