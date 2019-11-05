package org.smartframework.cloud.mask.serialize;

import java.lang.reflect.Field;

import org.smartframework.cloud.mask.MaskLog;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityHashMap;

public class MaskSerializeConfig extends SerializeConfig {

	public static final SerializeConfig GLOBAL_INSTANCE = new MaskSerializeConfig();

	private MaskSerializeConfig() {
		super(IdentityHashMap.DEFAULT_SIZE);
	}

	@Override
	public ObjectSerializer getObjectWriter(Class<?> clazz) {
		ObjectSerializer writer = get(clazz);
		if (writer == null) {
			writer = getObjectSerializer(clazz);
			if (writer != null) {
				// cache
				put(clazz, writer);
				return writer;
			}
		}

		return super.getObjectWriter(clazz);
	}

	private ObjectSerializer getObjectSerializer(Class<?> clazz) {
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(MaskLog.class)) {
					return new MaskSerializer(clazz);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

}