package org.smartframework.cloud.mask.serialize;

import java.lang.reflect.Field;

import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.util.MaskUtil;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc mask序列化器
 * @author liyulin
 * @date 2019/11/06
 */
@Slf4j
public class MaskSerializer extends JavaBeanSerializer {

	public MaskSerializer(Class<?> beanType) {
		super(beanType);
	}

	@Override
	protected Object processValue(JSONSerializer jsonBeanDeser, BeanContext beanContext, Object object, String key,
			Object propertyValue) {
		try {
			Field field = beanContext.getField();
			if (MaskSerializerHelper.isNeedMask(field)) {
				return MaskUtil.mask(propertyValue.toString(), field.getAnnotation(MaskLog.class));
			}
		} catch (SecurityException | IllegalArgumentException e) {
			log.error("process value error by reflection", e);
		}

		return super.processValue(jsonBeanDeser, beanContext, object, key, propertyValue);
	}

}