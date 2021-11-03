package org.smartframework.cloud.starter.core.constants;

import lombok.Getter;
import lombok.Setter;

/**
 * 当前系统环境
 *
 * @author collin
 * @date 2021-11-11
 */
public class SmartEnv {

    /**
     * 是否为单元测试环境
     */
    @Getter
    @Setter
    private static boolean unitTest = false;

}