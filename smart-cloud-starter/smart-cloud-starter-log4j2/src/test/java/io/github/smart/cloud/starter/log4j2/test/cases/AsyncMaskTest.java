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
package io.github.smart.cloud.starter.log4j2.test.cases;

import io.github.smart.cloud.starter.log.mask.pattern.PatternConfig;
import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import io.github.smart.cloud.starter.log4j2.test.prepare.App;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

/**
 * 异步appender脱敏测试：业务线程中设置脱敏上下文，日志线程中脱敏
 */
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AsyncMaskTest {

    private static final long WAIT_MILLIS = 5000L;
    /**
     * 输出到异步appender（见test/resources/log4j2.xml）
     */
    private static final Logger ASYNC_LOG = LoggerFactory.getLogger("async_mask_test");

    private Integer originalMode;

    @BeforeEach
    void setUp() {
        originalMode = PatternConfig.getMode();
    }

    @AfterEach
    void tearDown() {
        PatternConfig.setMode(originalMode);
        LogMaskContext.remove();
    }

    @Test
    void testAnnotationContextPassedToAsyncThread(CapturedOutput output) throws InterruptedException {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        try {
            LogMaskContext.set(MaskTest.REGEX);
            ASYNC_LOG.info("async1,姓名:{},测试", "张三");
        } finally {
            // 入队后立即清理业务线程上下文，验证日志线程使用的是入队时捕获的上下文
            LogMaskContext.remove();
        }

        String result = waitForOutput(output, "async1,");
        Assertions.assertThat(result).contains("async1,姓名:***,测试");
        // 调用位置信息（includeLocation）在业务线程中获取
        Assertions.assertThat(result).contains("[testAnnotationContextPassedToAsyncThread]");
    }

    @Test
    void testAnnotationWithoutContext(CapturedOutput output) throws InterruptedException {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        ASYNC_LOG.info("async2,姓名:{},测试", "张三");

        String result = waitForOutput(output, "async2,");
        Assertions.assertThat(result).contains("async2,姓名:张三,测试");
    }

    @Test
    void testFull(CapturedOutput output) throws InterruptedException {
        PatternConfig.setMode(MaskMode.FULL.getValue());
        ASYNC_LOG.info("async3，姓名:{}, mobile= {}.测试 GG", "张三", "1312134454");

        String result = waitForOutput(output, "async3，");
        Assertions.assertThat(result).contains("async3，姓名:***, mobile= *** GG");
    }

    /**
     * 等待异步线程输出包含指定标记的日志
     */
    private String waitForOutput(CapturedOutput output, String marker) throws InterruptedException {
        long deadline = System.currentTimeMillis() + WAIT_MILLIS;
        String result = output.getAll();
        while (!result.contains(marker) && System.currentTimeMillis() < deadline) {
            Thread.sleep(10);
            result = output.getAll();
        }
        return result;
    }

}
