package org.smartframework.cloud.starter.test.core;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.smartframework.cloud.starter.test.core.MockitoBeansTestExecutionListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * SpringBoot集成测试基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(value = MockitoBeansTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class AbstractIntegrationTest extends TestCase {

    static {
        String closeTag = Boolean.FALSE.toString();
        // 单元测试环境下，关闭依赖
        // 1.关闭api接口校验
        System.setProperty("smart.aspect.apiSecurity", closeTag);
        // 2.关闭api元数据上传
        System.setProperty("smart.uploadApiMeta", closeTag);
        // 3.关闭eureka
        System.setProperty("eureka.client.enabled", closeTag);
        // 4.单元测试环境下，关闭Sentinel自动化配置
        System.setProperty("spring.cloud.sentinel.enabled", closeTag);
    }

}