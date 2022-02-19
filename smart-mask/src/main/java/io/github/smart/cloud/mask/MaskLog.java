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
package io.github.smart.cloud.mask;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.smart.cloud.mask.jackson.MaskJsonSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @desc mask日志标记注解
 * @author collin
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