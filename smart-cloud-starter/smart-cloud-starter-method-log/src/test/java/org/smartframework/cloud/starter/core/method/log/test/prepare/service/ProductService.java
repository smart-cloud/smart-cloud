package org.smartframework.cloud.starter.core.method.log.test.prepare.service;

import org.smartframework.cloud.starter.core.method.log.annotation.MethodLog;
import org.springframework.stereotype.Component;

@Component
public class ProductService {

    @MethodLog
    public String query(Long id) {
        return String.valueOf(id);
    }

}