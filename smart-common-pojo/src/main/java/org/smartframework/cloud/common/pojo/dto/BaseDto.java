package org.smartframework.cloud.common.pojo.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 所有实体对象的基类
 *
 * @author liyulin
 * @date 2019年4月22日上午12:22:22
 */
@NoArgsConstructor
@SuperBuilder
public class BaseDto implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteBigDecimalAsPlain, SerializerFeature.WriteEnumUsingToString,
				// 禁用“循环引用检测”
				SerializerFeature.DisableCircularReferenceDetect);
	}

}