package org.smartframework.cloud.starter.rpc.test.pojo;

import org.smartframework.cloud.common.pojo.Base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class OrderVO extends Base{

	private static final long serialVersionUID = 1L;
	private String orderNo;
	
}