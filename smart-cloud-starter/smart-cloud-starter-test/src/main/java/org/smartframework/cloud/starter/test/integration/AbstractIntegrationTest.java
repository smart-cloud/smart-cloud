package org.smartframework.cloud.starter.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartframework.cloud.api.core.user.context.AbstractUserContext;
import org.smartframework.cloud.api.core.user.context.SmartUser;
import org.smartframework.cloud.starter.test.Constants;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.SerializingUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * SpringBoot集成测试基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class AbstractIntegrationTest {

    static {
        // 单元测试标志
        System.setProperty("smart.env.unittest", Boolean.TRUE.toString());

        String closeTag = Boolean.FALSE.toString();
        // 单元测试环境下，关闭依赖
        // 1.关闭api元数据上传
        System.setProperty("smart.uploadApiMeta", closeTag);
        // 2.关闭eureka
        System.setProperty("eureka.client.enabled", closeTag);
        // 3.单元测试环境下，关闭Sentinel自动化配置
        System.setProperty("spring.cloud.sentinel.enabled", closeTag);
    }

    protected abstract ApplicationContext getApplicationContext();

    @BeforeEach
    public void beforeTestMethod() {
        fillMockUserToContext();
    }

    /**
     * 填充当前线程上下文需要的用户信息
     */
    private static void fillMockUserToContext() {
        SmartUser smartUserMock = new SmartUser();
        smartUserMock.setId(1L);
        smartUserMock.setUsername("collin");
        smartUserMock.setRealName("Collin.Lee");
        smartUserMock.setMobile("13112341234");
        AbstractUserContext.setContext(smartUserMock);
    }

    private boolean enableRpcProtostuff() {
        return getApplicationContext().getEnvironment().getProperty(Constants.RPC_PROTOSTUFF_SWITCH, Boolean.class, true);
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
        if (resultBytes == null) {
            log.warn("test.result=null");
            return null;
        }

        if (url.contains("rpc") && enableRpcProtostuff()) {
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