package org.smartframework.cloud.mask;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.smartframework.cloud.mask.jackson.MaskJsonSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @desc mask日志标记注解
 * @author liyulin
 * @date 2019/11/05
 */
@Documented
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = MaskJsonSerializer.class) 
public @interface MaskLog {

	/** mask规则（当startLen、endLen、mask中有任一个被赋值时，value失效） */
	MaskRule value() default MaskRule.DEFAULT;

	/** 开头保留的长度 */
	int startLen() default DefaultMaskConfig.START_LEN;

	/** 结尾保留的长度 */
	int endLen() default DefaultMaskConfig.END_LEN;

	/** 掩码值 */
	String mask() default DefaultMaskConfig.MASK_TEXT;

}