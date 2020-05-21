package org.smartframework.cloud.mask.jackson;

import java.lang.annotation.Annotation;

import org.smartframework.cloud.mask.MaskLog;

import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

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