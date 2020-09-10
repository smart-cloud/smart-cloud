package org.smartframework.cloud.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页请求参数基类
 *
 * @author liyulin
 * @date 2020-05-07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePageReqVO<T> extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 分页请求参数（不包括分页信息）
     */
    @Valid
    private T query;

    /**
     * 第几页，第1页值为1
     */
    @NotNull
    @Min(value = 1)
    private Integer pageNum;

    /**
     * 页面数据大小
     */
    @NotNull
    @Min(value = 1)
    private Integer pageSize;

}