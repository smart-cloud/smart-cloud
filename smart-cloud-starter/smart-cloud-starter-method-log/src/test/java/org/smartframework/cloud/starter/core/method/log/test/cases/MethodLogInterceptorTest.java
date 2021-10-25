package org.smartframework.cloud.starter.core.method.log.test.cases;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.core.method.log.test.prepare.Application;
import org.smartframework.cloud.starter.core.method.log.test.prepare.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Slf4j
class MethodLogInterceptorTest {

    @Autowired
    private ProductService productService;

    @Test
    void testQuery() throws IOException {
        productService.query(100L);

        // 日志包含切面日志
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String fileAppenderName = "dailyRollingRandomAccessFile";
        RollingRandomAccessFileAppender appender = ctx.getConfiguration().getAppender(fileAppenderName);
        String fileName = appender.getFileName();
        String logContent = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
        String methodInterceptorLogKeys = "method.log=>";
        Assertions.assertThat(logContent).contains(methodInterceptorLogKeys);
    }

}
