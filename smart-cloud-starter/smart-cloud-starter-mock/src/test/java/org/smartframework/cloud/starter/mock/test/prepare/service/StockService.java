package org.smartframework.cloud.starter.mock.test.prepare.service;

import org.smartframework.cloud.starter.mock.annotation.Mock;
import org.smartframework.cloud.starter.mock.test.prepare.vo.DeductDTO;
import org.springframework.stereotype.Component;

@Component
public class StockService {

    @Mock
    public DeductDTO deduct() {
        return new DeductDTO(true);
    }

}