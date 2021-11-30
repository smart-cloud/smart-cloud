package org.smartframework.cloud.starter.rpc.dubbo.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * dubbo切面日志
 *
 * @author liyulin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"classMethod", "reqStartTime", "reqEndTime", "reqDealTime", "reqParams", "respData"})
public class DubboLogAspectDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调用的类方法
     */
    private String classMethod;

    /**
     * 请求发起时间
     */
    private Long startTime;

    /**
     * 请求结束时间
     */
    private Long endTime;

    /**
     * 请求处理时间,毫秒
     */
    private Integer cost;

    /**
     * 请求的参数信息
     */
    private Object params;

    /**
     * 响应数据
     */
    private Object result;

}