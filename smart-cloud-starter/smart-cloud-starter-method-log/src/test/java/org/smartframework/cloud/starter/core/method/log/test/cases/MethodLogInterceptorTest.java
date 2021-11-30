package org.smartframework.cloud.starter.core.method.log.test.cases;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.core.method.log.test.prepare.Application;
import org.smartframework.cloud.starter.core.method.log.test.prepare.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class MethodLogInterceptorTest {

    @Autowired
    private ProductService productService;

    @Test
    void testQuery() throws IOException {
        productService.query(100L);

        // 日志包含切面日志
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String appenderName = "console";
        ConsoleAppender appender = ctx.getConfiguration().getAppender(appenderName);
        ByteBuffer byteBuffer = appender.getManager().getByteBuffer().asReadOnlyBuffer();
        String logContent = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        byteBuffer.flip();
        Assertions.assertThat(StringUtils.containsAny(logContent, "method.info=>", "method.slow=>")).isTrue();
    }

}
