package org.smartframework.cloud.starter.mock.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mock.test.prepare.controller.OrderController;
import org.smartframework.cloud.starter.mock.test.prepare.vo.SubmitOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApiMockTest {

    @Autowired
    private OrderController orderController;

    @Test
    void testSubmit() {
        SubmitOrderResponse submitOrderResponse = orderController.submit();
        Assertions.assertThat(submitOrderResponse).isNotNull();
        Assertions.assertThat(submitOrderResponse.getResult()).isTrue();
    }

}