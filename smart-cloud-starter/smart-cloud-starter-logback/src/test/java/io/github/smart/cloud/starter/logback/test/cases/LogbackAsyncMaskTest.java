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
package io.github.smart.cloud.starter.logback.test.cases;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.OutputStreamAppender;
import io.github.smart.cloud.starter.log.mask.pattern.PatternConfig;
import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import io.github.smart.cloud.starter.logback.appender.MaskAsyncAppender;
import io.github.smart.cloud.starter.logback.test.prepare.App;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 异步appender脱敏测试：业务线程中设置脱敏上下文，日志线程中脱敏
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LogbackAsyncMaskTest {

    private static final long WAIT_MILLIS = 5000L;

    private Logger log;
    private ByteArrayOutputStream logOutput;
    private Integer originalMode;

    @BeforeEach
    @SuppressWarnings("rawtypes")
    void setUp() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        log = context.getLogger("async_mask_test");

        MaskAsyncAppender asyncAppender = (MaskAsyncAppender) log.getAppender("ASYNC_MEMORY");
        OutputStreamAppender target = (OutputStreamAppender) asyncAppender.getAppender("ASYNC_MEMORY_TARGET");
        logOutput = (ByteArrayOutputStream) target.getOutputStream();
        logOutput.reset();

        originalMode = PatternConfig.getMode();
    }

    @AfterEach
    void tearDown() {
        PatternConfig.setMode(originalMode);
        LogMaskContext.remove();
    }

    @Test
    void testAnnotationContextPassedToAsyncThread() throws InterruptedException {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        try {
            LogMaskContext.set(LogbackMaskTest.REGEX);
            log.info("XXX,姓名:{},测试", "张三");
        } finally {
            // 入队后立即清理业务线程上下文，验证日志线程使用的是入队时捕获的上下文
            LogMaskContext.remove();
        }

        String result = waitForOutput();
        System.out.print(result);
        Assertions.assertThat(result).contains("XXX,姓名:***,测试");
        // 调用者信息在业务线程中获取，日志线程中仍可正确输出
        Assertions.assertThat(result).contains("testAnnotationContextPassedToAsyncThread");
    }

    @Test
    void testAnnotationWithoutContext() throws InterruptedException {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        log.info("XXX,姓名:{},测试", "张三");

        String result = waitForOutput();
        System.out.print(result);
        Assertions.assertThat(result).contains("XXX,姓名:张三,测试");
    }

    @Test
    void testFull() throws InterruptedException {
        PatternConfig.setMode(MaskMode.FULL.getValue());
        log.info("xx，姓名:{}, mobile= {}.测试 GG", "张三", "1312134454");

        String result = waitForOutput();
        System.out.print(result);
        Assertions.assertThat(result).contains("xx，姓名:***, mobile= *** GG");
    }

    /**
     * 等待异步线程输出一行日志
     */
    private String waitForOutput() throws InterruptedException {
        long deadline = System.currentTimeMillis() + WAIT_MILLIS;
        String result = readOutput();
        while (!result.contains("\n") && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
            result = readOutput();
        }
        return result;
    }

    private String readOutput() {
        return new String(logOutput.toByteArray(), StandardCharsets.UTF_8);
    }

}
