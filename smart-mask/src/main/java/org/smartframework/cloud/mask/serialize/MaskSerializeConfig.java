package org.smartframework.cloud.mask.serialize;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IdentityHashMap;
import org.smartframework.cloud.mask.EnableMask;

/**
 * @author liyulin
 * @desc mask序列化器配置
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
		if (serializer == null && clazz.isAnnotationPresent(EnableMask.class)) {
			serializer = new MaskSerializer(clazz);
			put(clazz, serializer);
			return serializer;
		}

		return super.getObjectWriter(clazz);
	}

}