package org.smartframework.cloud.common.pojo.dto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页请求参数基类")
public class BasePageReq<T extends BaseDto> extends BaseDto {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "分页请求参数（不包括分页信息）")
	@Valid
	private T query;

	@ApiModelProperty(value = "第几页，第1页值为1", required = true, example = "1")
	@NotNull
	@Min(value = 1)
	private Integer pageNum;

	@ApiModelProperty(value = "页面数据大小", required = true, example = "10")
	@NotNull
	@Min(value = 1)
	private Integer pageSize;

}