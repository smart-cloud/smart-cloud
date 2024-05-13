package io.github.smart.cloud.starter.monitor.actuator.test.cases;

import io.github.smart.cloud.starter.monitor.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.monitor.actuator.notify.http.ExceptionApiChecker;
import io.github.smart.cloud.starter.monitor.actuator.repository.ApiHealthRepository;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.App;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.controller.OrderController;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.openfeign.IOrderFeign;
import io.github.smart.cloud.starter.monitor.actuator.test.prepare.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class, args = "--spring.profiles.active=exception-api-checker", webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExceptionApiTest extends AbstractTest {

    @Autowired
    private ExceptionApiChecker exceptionApiChecker;
    @Autowired
    private ApiHealthRepository apiHealthRepository;

    @Test
    void testExceptionApiCheck() throws Exception {
        OrderController orderController = applicationContext.getBean(OrderController.class);
        for (int i = 1; i <= 6; i++) {
            try {
                orderController.query(i);
            } catch (Exception e) {

            }
        }
        for (int i = 95; i <= 110; i++) {
            try {
                orderController.buy(i);
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

        // 失败率倒叙测试
        List<UnHealthApiDTO> unHealthInfos = apiHealthRepository.getUnHealthInfos();
        Assertions.assertThat(unHealthInfos).hasSize(2);

        UnHealthApiDTO unHealthApi0 = unHealthInfos.get(0);
        UnHealthApiDTO unHealthApi1 = unHealthInfos.get(1);
        BigDecimal fail0 = BigDecimal.valueOf(unHealthApi0.getFailCount()).divide(BigDecimal.valueOf(unHealthApi0.getTotal()), 10, RoundingMode.HALF_UP);
        BigDecimal fail1 = BigDecimal.valueOf(unHealthApi1.getFailCount()).divide(BigDecimal.valueOf(unHealthApi1.getTotal()), 10, RoundingMode.HALF_UP);
        Assertions.assertThat(fail0).isGreaterThanOrEqualTo(fail1);


        Assertions.assertThat(exceptionApiChecker.checkExceptionApiAndNotice()).isTrue();
    }

    @Test
    void testApiHealthMonitor() {
        ProductService productService = applicationContext.getBean(ProductService.class);
        int[] ids = {5, 5, 1};
        for (int id : ids) {
            try {
                productService.query(id);
            } catch (Exception e) {
            }
        }


        List<UnHealthApiDTO> unHealthInfos = apiHealthRepository.getUnHealthInfos();
        Assertions.assertThat(unHealthInfos).hasSize(1);
    }

}