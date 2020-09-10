package org.smartframework.cloud.starter.rpc.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;

/**
 * feign切面日志Dto
 *
 * @author liyulin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FeignLogAspectDO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 调用的类方法
     */
    private String classMethod;

    /**
     * 请求处理时间,毫秒
     */
    private long cost;

    /**
     * 请求的参数信息
     */
    private Object reqParams;

    /**
     * 请求头
     */
    private Object reqHeaders;

    /**
     * 响应数据
     */
    private Object respData;

}