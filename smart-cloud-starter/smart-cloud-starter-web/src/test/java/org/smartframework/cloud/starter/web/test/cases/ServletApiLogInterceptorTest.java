package org.smartframework.cloud.starter.web.test.cases;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.starter.web.test.prepare.Application;
import org.smartframework.cloud.starter.web.test.prepare.controller.ProductController;
import org.smartframework.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class ServletApiLogInterceptorTest {

    @Autowired
    private ProductController productController;

    @Test
    void testQuery() throws IOException {
        Response<String> response = productController.query(100L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
        Assertions.assertThat(response.getBody()).isNotBlank();

        checkLog();
    }


    @Test
    void testCreate() throws IOException {
        ProductCreateReqVO reqVO = new ProductCreateReqVO();
        reqVO.setDesc("手机");
        reqVO.setName("iphone");
        Response<Boolean> response = productController.create(reqVO);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getHead()).isNotNull();
        Assertions.assertThat(response.getHead().getCode()).isEqualTo(CommonReturnCodes.SUCCESS.getCode());
        Assertions.assertThat(response.getBody()).isTrue();

        checkLog();
    }

    /**
     * 日志包含切面日志
     *
     * @throws IOException
     */
    private void checkLog() throws IOException {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String fileAppenderName = "dailyRollingRandomAccessFile";
        RollingRandomAccessFileAppender appender = ctx.getConfiguration().getAppender(fileAppenderName);
        String fileName = appender.getFileName();
        String logContent = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
        String methodInterceptorLogKeys = "api.info=>";
        Assertions.assertThat(logContent).contains(methodInterceptorLogKeys);
    }

}