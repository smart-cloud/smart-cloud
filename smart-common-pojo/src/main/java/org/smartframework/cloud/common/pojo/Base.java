package org.smartframework.cloud.common.pojo;

import java.io.Serializable;

import org.smartframework.cloud.utility.JacksonUtil;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * pojo基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@NoArgsConstructor
@SuperBuilder
public class Base implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return JacksonUtil.toJson(this);
	}

}