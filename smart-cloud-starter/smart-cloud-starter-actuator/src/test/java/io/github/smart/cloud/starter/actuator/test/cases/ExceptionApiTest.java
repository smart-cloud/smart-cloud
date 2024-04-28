package io.github.smart.cloud.starter.actuator.test.cases;

import io.github.smart.cloud.starter.actuator.notify.http.ExceptionApiChecker;
import io.github.smart.cloud.starter.actuator.test.prepare.App;
import io.github.smart.cloud.starter.actuator.test.prepare.controller.OrderController;
import io.github.smart.cloud.starter.actuator.test.prepare.openfeign.IOrderFeign;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, args = "--spring.profiles.active=exception-api-checker", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExceptionApiTest extends AbstractTest {

    @Autowired
    private ExceptionApiChecker exceptionApiChecker;

    @Test
    void testExceptionApiCheck() throws Exception {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        for (int i = 1; i <= 6; i++) {
            try {
                orderController.query(i);
            } catch (Exception e) {

            }
        }

        IOrderFeign orderFeign = applicationContext.getBean(IOrderFeign.class);
        int[] ids = {5, 5, 1};
        for (int id : ids) {
            try {
                orderFeign.query(id);
            } catch (Exception e) {
            }
        }

        Assertions.assertThat(exceptionApiChecker.checkExceptionApiAndNotice()).isTrue();
    }

}