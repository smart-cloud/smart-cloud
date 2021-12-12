/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartframework.cloud.mask.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.smartframework.cloud.mask.DefaultMaskConfig;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.util.MaskUtil;

import java.io.IOException;

/**
 * {@link MaskLog}注解脱敏处理
 *
 * @author collin
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
            throws IOException {
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
