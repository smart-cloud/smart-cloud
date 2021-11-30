package org.smartframework.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

/**
 * 切面配置
 *
 * @author liyulin
 * @date 2019-06-19
 */
@Getter
@Setter
public class LogProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * feign切面开关 （默认false）
     */
    private boolean rpclog = false;
    /**
     * 接口日志切面开关 （默认false）
     */
    private boolean apilog = false;
    /**
     * 慢接口时间（单位：毫秒，默认3秒）
     */
    private int slowApiMinCost = 3000;

}