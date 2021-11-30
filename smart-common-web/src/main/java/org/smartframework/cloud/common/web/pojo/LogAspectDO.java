package org.smartframework.cloud.common.web.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;

/**
 * 普通接口（除了feign以外的接口）切面日志DO
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
@JsonPropertyOrder({"url", "method", "cost", "head", "queryParams", "args", "result"})
public class LogAspectDO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 请求路径
     */
    private String url;
    /**
     * http请求方式
     */
    private String method;
    /**
     * http头部数据
     */
    private Object head;
    /**
     * url参数
     */
    private String queryParams;
    /**
     * body部分请求体参数
     */
    private Object args;
    /**
     * 请求结果
     */
    private Object result;
    /**
     * 花费时间（毫秒）
     */
    private Long cost;

}