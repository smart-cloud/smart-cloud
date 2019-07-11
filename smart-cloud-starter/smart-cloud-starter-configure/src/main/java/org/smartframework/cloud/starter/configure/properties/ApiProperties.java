package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * api配置
 * 
 * @author liyulin
 * @date 2019年6月19日 下午10:24:02
 */
@Getter
@Setter
public class ApiProperties extends BaseDto {

	private static final long serialVersionUID = 1L;
	/** hibernate validator开关 （默认false） */
	private boolean validator = false;
	/** api版本 */
	private String apiVersion;
	
}