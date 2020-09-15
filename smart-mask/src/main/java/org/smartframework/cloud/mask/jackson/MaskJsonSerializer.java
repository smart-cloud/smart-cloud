package org.smartframework.cloud.mask.jackson;

import java.io.IOException;

import org.smartframework.cloud.mask.DefaultMaskConfig;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.util.MaskUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * {@link MaskLog}注解脱敏处理
 *
 * @author liyulin
 * @date 2020-05-30
 */
public class MaskJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

	private MaskLog maskLog;

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		if (property != null) {
			maskLog = property.getAnnotation(MaskLog.class);
		}
		return this;
	}

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (maskLog == null || value == null) {
			gen.writeObject(value);
			return;
		}

		int startLen, endLen;
		String mask;
		if (isSetMaskAttributes(maskLog)) {
			startLen = maskLog.startLen();
			endLen = maskLog.endLen();
			mask = maskLog.mask();
		} else {
			MaskRule maskRule = maskLog.value();
			startLen = maskRule.getStartLen();
			endLen = maskRule.getEndLen();
			mask = maskRule.getMask();
		}

		gen.writeObject(MaskUtil.mask(value, startLen, endLen, mask));
	}

	private boolean isSetMaskAttributes(MaskLog maskLog) {
		return maskLog.startLen() != DefaultMaskConfig.START_LEN || maskLog.endLen() != DefaultMaskConfig.END_LEN
				|| !DefaultMaskConfig.MASK_TEXT.equals(maskLog.mask());
	}

}
