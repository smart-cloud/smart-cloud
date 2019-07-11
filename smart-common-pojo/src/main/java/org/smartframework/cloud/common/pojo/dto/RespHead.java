package org.smartframework.cloud.common.pojo.dto;

import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import uk.co.jemos.podam.common.PodamStringValue;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ApiModel(description = "响应头部")
public class RespHead extends BaseDto {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "全局唯一交易流水号", example = "eb9f81e7cee1c000")
	private String nonce;

	@ApiModelProperty(value = "响应状态码", example = "100500")
	@PodamStringValue(strValue = "100200")
	private String code;

	@ApiModelProperty(value = "提示信息", example = "服务器异常")
	private String message;

	@ApiModelProperty(value = "响应时间戳", example = "1554551377629")
	private long timestamp;
	
	public RespHead(IBaseReturnCode returnCode) {
		setReturnCode(returnCode);
	}

	public RespHead(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public RespHead(IBaseReturnCode returnCode, String message) {
		if (returnCode != null) {
			this.code = returnCode.getCode();
		}
		this.message = message;
	}

	public void setReturnCode(IBaseReturnCode returnCode) {
		if (returnCode == null) {
			return;
		}

		this.code = returnCode.getCode();
		this.message = returnCode.getMessage();
	}

}