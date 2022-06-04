package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.test.prepare.service;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicRestHighLevelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final DynamicRestHighLevelClient dynamicRestHighLevelClient;

}