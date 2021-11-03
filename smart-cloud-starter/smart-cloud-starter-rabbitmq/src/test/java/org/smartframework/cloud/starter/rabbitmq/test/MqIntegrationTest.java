package org.smartframework.cloud.starter.rabbitmq.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.rabbitmq.test.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MqIntegrationTest {

    @Autowired
    private Producer producer;

    @Test
    void testRetry() throws InterruptedException {
        producer.send(200L);
        TimeUnit.SECONDS.sleep(120);
    }

}