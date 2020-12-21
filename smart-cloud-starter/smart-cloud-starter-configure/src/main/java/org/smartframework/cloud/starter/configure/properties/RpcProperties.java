package org.smartframework.cloud.starter.configure.properties;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

/**
 * rpc配置
 *
 * @author collin
 * @date 2020-12-21
 */
@Getter
@Setter
public class RpcProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 接口响应是否开启protostuff（默认开启）
     */
    private boolean protostuff = true;

}