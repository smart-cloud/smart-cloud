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
package io.github.smart.cloud.mask.jackson;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import io.github.smart.cloud.mask.MaskLog;

import java.lang.annotation.Annotation;

/**
 * 使{@link MaskLog}注解生效，相当于{@code @JacksonAnnotationsInside}的作用
 *
 * @author collin
 * @date 2020-05-30
 */
public class MaskJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean isAnnotationBundle(Annotation ann) {
        if (ann.annotationType() == MaskLog.class) {
            return true;
        } else {
            return super.isAnnotationBundle(ann);
        }
    }

}