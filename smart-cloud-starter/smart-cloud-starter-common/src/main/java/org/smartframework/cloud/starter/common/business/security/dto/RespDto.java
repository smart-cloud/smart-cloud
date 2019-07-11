package org.smartframework.cloud.starter.common.business.security.dto;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "响应信息体（加密+签名）")
public class RespDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "加密后的head")
	private String head;
	/** 加密后的body */
	@ApiModelProperty(value = "加密后的body")
	private String body;
	/** 签名 */
	@ApiModelProperty(value = "签名")
	private String sign;

}