package org.smartframework.cloud.common.pojo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
@ApiModel(description = "分页请求响应信息")
public class BasePageResp<T extends BaseDto> extends BaseDto {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "响应信息")
	private List<T> datas;

	@ApiModelProperty(value = "页数")
	protected int pageIndex;

	@ApiModelProperty(value = "页码大小")
	protected int pageSize;

	@ApiModelProperty(value = "总数据条数")
	private long pageTotal;

	public List<T> getDatas() {
		if (Objects.isNull(datas)) {
			datas = new ArrayList<>();
		}

		return datas;
	}
	
}