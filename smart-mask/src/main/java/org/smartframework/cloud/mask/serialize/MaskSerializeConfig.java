package org.smartframework.cloud.mask.serialize;

import java.lang.reflect.Field;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityHashMap;

/**
 * @desc mask序列化器配置
 * @author liyulin
 * @date 2019/11/06
 */
public class MaskSerializeConfig extends SerializeConfig {

	public static final SerializeConfig GLOBAL_INSTANCE = new MaskSerializeConfig();

	private MaskSerializeConfig() {
		super(IdentityHashMap.DEFAULT_SIZE);
	}

	@Override
	public ObjectSerializer getObjectWriter(Class<?> clazz) {
		ObjectSerializer serializer = get(clazz);
		if (serializer == null) {
			serializer = getMaskObjectSerializer(clazz);
			if (serializer != null) {
				// cache
				put(clazz, serializer);
				return serializer;
			}
		}

		return super.getObjectWriter(clazz);
	}

	private ObjectSerializer getMaskObjectSerializer(Class<?> clazz) {
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (MaskSerializeHelper.isNeedMask(field)) {
					return new MaskSerializer(clazz);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

}