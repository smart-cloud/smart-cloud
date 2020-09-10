package org.smartframework.cloud.starter.test.integration;

import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 每个case执行完后，清除mock设置
 *
 * @author liyulin
 * @date 2020-09-03
 */
public class MockitoBeansTestExecutionListener extends AbstractTestExecutionListener {

    private List<Object> injectMockBeans = new ArrayList<>();
    private boolean init = false;

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        if (init) {
            return;
        }

        Field[] declaredFields = testContext.getTestClass().getDeclaredFields();
        //将需要mock注入的对象存储起来，以便case执行完后清除设置
        for (Field field : declaredFields) {
            if (field.getAnnotation(MockBean.class) != null || field.getAnnotation(SpyBean.class) != null) {
                field.setAccessible(true);
                Object mockBean = field.get(testContext.getTestInstance());
                injectMockBeans.add(mockBean);
            }
        }

        init = true;
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        if (injectMockBeans.isEmpty()) {
            return;
        }
        // 清除mock设置
        injectMockBeans.forEach(Mockito::reset);
    }

}