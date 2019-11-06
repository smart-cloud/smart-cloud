package org.smartframework.cloud.mask.serialize;

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
		ObjectSerializer serializer = super.get(clazz);
		if (serializer == null) {
			serializer = new MaskSerializer(clazz);
			// cache
			super.put(clazz, serializer);
		}

		return serializer;
	}

}