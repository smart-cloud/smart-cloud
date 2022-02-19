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
package io.github.smart.cloud.starter.rabbitmq.test.cases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.smart.cloud.starter.rabbitmq.test.prepare.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

class MqIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private Producer producer;

    @Test
    void testRetry() throws InterruptedException {
        producer.send(200L);
        TimeUnit.SECONDS.sleep(95);

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        String appenderName = "console";
        ConsoleAppender appender = ctx.getConfiguration().getAppender(appenderName);
        ByteBuffer byteBuffer = appender.getManager().getByteBuffer().asReadOnlyBuffer();
        String logContent = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        byteBuffer.flip();
        Assertions.assertThat(logContent).contains("Maximum times of retries reached");
    }

}