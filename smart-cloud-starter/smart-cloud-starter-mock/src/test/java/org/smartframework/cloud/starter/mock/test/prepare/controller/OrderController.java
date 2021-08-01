package org.smartframework.cloud.starter.mock.test.prepare.controller;

import org.smartframework.cloud.starter.mock.test.prepare.service.StockService;
import org.smartframework.cloud.starter.mock.test.prepare.vo.SubmitOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private StockService stockService;

    @PostMapping("submit")
    public SubmitOrderResponse submit() {
        return new SubmitOrderResponse(stockService.deduct().getResult());
    }

}