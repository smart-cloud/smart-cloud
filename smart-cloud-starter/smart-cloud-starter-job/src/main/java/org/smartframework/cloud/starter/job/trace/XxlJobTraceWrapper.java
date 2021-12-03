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
package org.smartframework.cloud.starter.job.trace;

import brave.ScopedSpan;
import brave.Tracing;
import com.xxl.job.core.handler.IJobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.internal.DefaultSpanNamer;

/**
 * xxl-job执行包装类
 *
 * @author collin
 * @date 2021-04-11
 */
@Slf4j
@RequiredArgsConstructor
public class XxlJobTraceWrapper extends IJobHandler {

    private final BeanFactory beanFactory;

    private final IJobHandler delegate;

    private Tracing tracing;

    private SpanNamer spanNamer;

    @Override
    public void execute() throws Exception {
        if (tracing == null) {
            try {
                tracing = beanFactory.getBean(Tracing.class);
            } catch (NoSuchBeanDefinitionException e) {
                delegate.execute();
                return;
            }
        }
        doExecute();
    }

    private void doExecute() throws Exception {
        ScopedSpan span = tracing.tracer().startScopedSpanWithParent(spanNamer().name(delegate, "xxl-job"),
                tracing.currentTraceContext().get());
        try {
            delegate.execute();
        } catch (Exception | Error e) {
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    /**
     * due to some race conditions trace keys might not be ready yet
     *
     * @return
     */
    private SpanNamer spanNamer() {
        if (spanNamer == null) {
            try {
                spanNamer = beanFactory.getBean(SpanNamer.class);
            } catch (NoSuchBeanDefinitionException e) {
                log.warn("SpanNamer bean not found - will provide a manually created instance");
                return new DefaultSpanNamer();
            }
        }
        return spanNamer;
    }

}