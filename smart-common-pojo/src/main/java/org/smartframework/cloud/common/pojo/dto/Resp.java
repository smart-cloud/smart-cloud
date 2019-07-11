package org.smartframework.cloud.common.pojo.dto;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "响应对象")
public class Resp<T extends BaseDto> extends BaseDto {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "响应头部")
	private RespHead head = null;

	@ApiModelProperty(value = "响应体")
	private T body;

	@ApiModelProperty(value = "签名")
	private String sign;

	public Resp(RespHead head) {
		this.head = head;
	}

	public Resp(T body) {
		this.head = new RespHead(ReturnCodeEnum.SUCCESS);
		this.body = body;
	}

}