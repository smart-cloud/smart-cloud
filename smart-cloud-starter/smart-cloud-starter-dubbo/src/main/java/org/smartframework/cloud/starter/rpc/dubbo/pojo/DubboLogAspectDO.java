package org.smartframework.cloud.starter.rpc.dubbo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.utility.constant.DateFormartConst;

import java.io.Serializable;
import java.util.Date;

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
    @JsonFormat(pattern = DateFormartConst.DATETIME_SSS)
    private Date reqStartTime;

    /**
     * 请求结束时间
     */
    @JsonFormat(pattern = DateFormartConst.DATETIME_SSS)
    private Date reqEndTime;

    /**
     * 请求处理时间,毫秒
     */
    private Integer reqDealTime;

    /**
     * 请求的参数信息
     */
    private Object reqParams;

    /**
     * 响应数据
     */
    private Object respData;

}