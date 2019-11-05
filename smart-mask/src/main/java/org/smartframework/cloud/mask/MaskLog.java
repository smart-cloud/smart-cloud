package org.smartframework.cloud.mask;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @desc mask日志标记注解
 * @author liyulin
 * @date 2019/11/05
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskLog {

	/** mask规则 */
	MaskRule value() default MaskRule.DEFAULT;

	/** 开头保留的长度 */
	int startLen() default MaskConstants.START_LEN;

	/** 结尾保留的长度 */
	int endLen() default MaskConstants.END_LEN;

	/** 掩码值 */
	String mask() default MaskConstants.DEFAULT_MASK_TEXT;

}