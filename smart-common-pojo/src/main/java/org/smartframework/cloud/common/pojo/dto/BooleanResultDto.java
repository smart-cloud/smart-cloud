package org.smartframework.cloud.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * boolean dto
 * 
 * @author liyulin
 * @date 2019年7月5日 下午1:26:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResultDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	/**是否成功（通过）*/
	private boolean success;
	
	/**提示信息*/
	private String message;
	
	public BooleanResultDto(boolean success) {
		this.success = success;
	}
	
}