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
import io.github.smart.cloud.starter.log.mask.pattern.PatternConfig;
import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import io.github.smart.cloud.starter.logback.test.prepare.App;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LogbackMaskTest {
    /**
     * 自定义正则表达式
     */
    public static final String REGEX = "\\s*((?:\\\\\"|[\"'])?[(姓名|mobile|公司)]+(?:\\\\\"|[\"'])?)(\\s*[:：=]+)\\s*((?:\\\\\"|[\"'])?[^,\\[\\]{};，；\\s]+(?:\\\\\"|[\"'])?)\\s*";

    private ByteArrayOutputStream logOutput;
    private Logger log;

    @PostConstruct
    public void setUp() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        log = context.getLogger(Logger.ROOT_LOGGER_NAME);

        // 获取内存Appender的输出流
        ch.qos.logback.core.OutputStreamAppender appender =
                (ch.qos.logback.core.OutputStreamAppender) log.getAppender("MEMORY");
        logOutput = (ByteArrayOutputStream) appender.getOutputStream();
    }

    @Test
    public void testOff() {
        PatternConfig.setMode(MaskMode.OFF.getValue());
        try {
            LogMaskContext.set(REGEX);
            String name = "张三";
            String mobile = "1312134454";
            log.info("XXX,姓名:{},测试", name);
            log.info("XXX,mobile:{},测试", mobile);
            log.info("xx，姓名:{}, mobile= {}.测试", name, mobile);
        } finally {
            LogMaskContext.remove();
        }
    }

    @Test
    public void testAnnotation() {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        try {
            LogMaskContext.set(REGEX);
            String name = "张三";
            String mobile = "1312134454";
            String company = "少时诵诗书";

            logOutput.reset();
            log.info("XXX,姓名:{},测试", name);
            String result1 = logOutput.toString();
            System.out.print(result1);
            Assertions.assertThat(result1).contains("XXX,姓名:***,测试");

            logOutput.reset();
            log.info("XXX,mobile:{},测试", mobile);
            String result2 = logOutput.toString();
            System.out.print(result2);
            Assertions.assertThat(result2).contains("XXX,mobile:***,测试");

            logOutput.reset();
            log.info("xx，姓名:{}, mobile= {}.测试,公司={}", name, mobile, company);
            String result3 = logOutput.toString();
            System.out.print(result3);
            Assertions.assertThat(result3).contains("xx，姓名:***,mobile=***,公司=***");
        } finally {
            LogMaskContext.remove();
        }
    }


    @Test
    public void testFull() {
        PatternConfig.setMode(MaskMode.FULL.getValue());
        String name = "张三";
        String mobile = "1312134454";

        logOutput.reset();
        log.info("XXX,姓名:{},测试", name);
        String result1 = logOutput.toString();
        System.out.print(result1);
        Assertions.assertThat(result1).contains("XXX,姓名:***,测试");

        logOutput.reset();
        log.info("XXX,mobile:{},测试", mobile);
        String result2 = logOutput.toString();
        System.out.print(result2);
        Assertions.assertThat(result2).contains("XXX,mobile:***,测试");

        logOutput.reset();
        log.info("xx，姓名:{}, mobile= {}.测试 GG", name, mobile);
        String result3 = logOutput.toString();
        System.out.print(result3);
        Assertions.assertThat(result3).contains("xx，姓名:***, mobile= *** GG");
    }

}