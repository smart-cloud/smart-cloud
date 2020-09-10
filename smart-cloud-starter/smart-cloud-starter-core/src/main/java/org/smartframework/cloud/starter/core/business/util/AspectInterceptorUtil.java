package org.smartframework.cloud.starter.core.business.util;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * 切面拦截器工具类
 *
 * @author liyulin
 * @date 2019-04-21
 */
@UtilityClass
public class AspectInterceptorUtil {

    /**
     * 获取被注解标记的类切面表达式
     *
     * @param annotations
     * @return
     */
    public static String getWithinExpression(List<Class<? extends Annotation>> annotations) {
        StringBuilder expression = new StringBuilder();
        for (int i = 0; i < annotations.size(); i++) {
            expression.append("@within(" + annotations.get(i).getTypeName() + ")");
            if (i != annotations.size() - 1) {
                expression.append(" || ");
            }
        }
        return expression.toString();
    }

    /**
     * 获取接口切面表达式
     *
     * @param basePackages
     * @return
     */
    public static String getApiExpression(String[] basePackages) {
        String controllerExpression = getWithinExpression(Arrays.asList(Controller.class, RestController.class));

        StringBuilder executions = new StringBuilder();
        for (int i = 0; i < basePackages.length; i++) {
            executions.append("execution( * " + basePackages[i] + "..*.*(..))");
            if (i != basePackages.length - 1) {
                executions.append(" || ");
            }
        }

        return "(" + executions + ") && (" + controllerExpression + ")";
    }

}