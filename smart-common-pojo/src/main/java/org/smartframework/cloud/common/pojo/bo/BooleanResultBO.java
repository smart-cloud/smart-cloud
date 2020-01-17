package org.smartframework.cloud.common.pojo.bo;

import org.smartframework.cloud.common.pojo.Base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * boolean BO
 * 
 * @author liyulin
 * @date 2019-07-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResultBO extends Base {

	private static final long serialVersionUID = 1L;
	
	/**是否成功（通过）*/
	private boolean success;
	
	/**提示信息*/
	private String message;
	
	public BooleanResultBO(boolean success) {
		this.success = success;
	}
	
}