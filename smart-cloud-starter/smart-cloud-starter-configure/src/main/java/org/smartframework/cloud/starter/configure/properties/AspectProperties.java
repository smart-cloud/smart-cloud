package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.common.pojo.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 切面配置
 * 
 * @author liyulin
 * @date 2019年6月19日 下午10:20:54
 */
@Getter
@Setter
public class AspectProperties extends BaseDto {

	private static final long serialVersionUID = 1L;
	/** 重复提交校验切面开关 （默认false） */
	private boolean repeatSubmitCheck = false;
	/** feign切面开关 （默认false） */
	private boolean rpclog = false;
	/** feign加密、签名切面开关 （默认false） */
	private boolean rpcSecurity = false;
	/** 接口日志切面开关 （默认false） */
	private boolean apilog = false;
	/** 接口加密、签名切面开关（默认false） */
	private boolean apiSecurity = false;
	/** mock开关 （默认false） */
	private boolean mock = false;

}