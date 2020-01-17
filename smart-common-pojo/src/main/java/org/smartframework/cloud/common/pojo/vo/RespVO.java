package org.smartframework.cloud.common.pojo.vo;

import org.smartframework.cloud.common.pojo.Base;
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
public class RespVO<T extends Base> extends Base {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "响应头部")
	private RespHeadVO head = null;

	@ApiModelProperty(value = "响应体")
	private T body;

	@ApiModelProperty(value = "签名")
	private String sign;

	public RespVO(RespHeadVO head) {
		this.head = head;
	}

	public RespVO(T body) {
		this.head = new RespHeadVO(ReturnCodeEnum.SUCCESS);
		this.body = body;
	}

}