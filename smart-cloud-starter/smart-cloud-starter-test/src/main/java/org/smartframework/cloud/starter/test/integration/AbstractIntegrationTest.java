package org.smartframework.cloud.starter.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.SerializingUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

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
        // 1.关闭api元数据上传
        System.setProperty("smart.uploadApiMeta", closeTag);
        // 2.关闭eureka
        System.setProperty("eureka.client.enabled", closeTag);
        // 3.单元测试环境下，关闭Sentinel自动化配置
        System.setProperty("spring.cloud.sentinel.enabled", closeTag);
    }

    /**
     * 序列化响应（主要处理rpc结果）
     *
     * @param resultBytes
     * @param typeReference
     * @param url
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T deserializeResponse(byte[] resultBytes, TypeReference<T> typeReference, String url) throws IOException {
        if (url.contains("rpc")) {
            // 处理rpc返回结果（protostuff反序列化）
            Class c = null;
            if (typeReference.getType() instanceof ParameterizedType) {
                c = (Class) ((ParameterizedType) typeReference.getType()).getRawType();
            } else {
                c = (Class) typeReference.getType();
            }
            T t = (T) SerializingUtil.deserialize(resultBytes, c);
            log.info("test.result={}", JacksonUtil.toJson(t));
            return t;
        } else {
            String content = new String(resultBytes);
            log.info("test.result={}", content);

            return JacksonUtil.parseObject(content, typeReference);
        }
    }

}