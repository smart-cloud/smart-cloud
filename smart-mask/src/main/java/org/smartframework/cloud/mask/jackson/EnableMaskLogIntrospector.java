package org.smartframework.cloud.mask.jackson;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.smartframework.cloud.mask.MaskLog;

import java.lang.annotation.Annotation;

/**
 * 使{@link MaskLog}注解生效，相当与{@code @JacksonAnnotationsInside}的作用
 *
 * @author liyulin
 * @date 2020-05-30
 */
public class EnableMaskLogIntrospector extends JacksonAnnotationIntrospector {

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