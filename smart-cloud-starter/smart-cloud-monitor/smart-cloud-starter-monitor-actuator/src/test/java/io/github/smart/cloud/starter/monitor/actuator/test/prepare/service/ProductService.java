package io.github.smart.cloud.starter.monitor.actuator.test.prepare.service;

import io.github.smart.cloud.starter.monitor.actuator.annotation.ApiHealthMonitor;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @ApiHealthMonitor
    public int query(int id) {
        if (id % 5 > 0) {
            throw new UnsupportedOperationException();
        }
        return id;
    }

}