package org.smartframework.cloud.starter.mock.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.starter.mock.test.prepare.service.StockService;
import org.smartframework.cloud.starter.mock.test.prepare.vo.DeductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MethodMockTest {

    @Autowired
    private StockService stockService;

    @Test
    void testDeduct() {
        DeductDTO deductDTO = stockService.deduct();
        Assertions.assertThat(deductDTO).isNotNull();
        Assertions.assertThat(deductDTO.getResult()).isFalse();
    }

}