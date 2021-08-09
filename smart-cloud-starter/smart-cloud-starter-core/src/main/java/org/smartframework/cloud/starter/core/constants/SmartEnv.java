package org.smartframework.cloud.starter.core.constants;

import lombok.Getter;
import lombok.Setter;

public class SmartEnv {

    /**
     * 是否为单元测试环境
     */
    @Getter
    @Setter
    private static boolean unitTest = false;

}