package org.smartframework.cloud.starter.rabbitmq.test.cases;

import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.rabbitmq.test.prepare.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

class MqIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private Producer producer;

    @Test
    void testRetry() throws InterruptedException {
        producer.send(200L);
        TimeUnit.SECONDS.sleep(100);
    }

}