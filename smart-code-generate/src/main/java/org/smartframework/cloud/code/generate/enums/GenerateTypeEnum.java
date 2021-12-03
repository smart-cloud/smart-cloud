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
package org.smartframework.cloud.code.generate.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表生成类型（1、数据库整个表全部生成；2、只生成指定的表；3、除了指定的表，全部生成）
 *
 * @author liyulin
 * @date 2019-07-14
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GenerateTypeEnum {

	/** 数据库整个表全部生成 */
	ALL(1),
	/** 只生成指定的表 */
	INCLUDE(2),
	/** 除了指定的表，全部生成 */
	EXCLUDE(3);

	/** 生成类型 */
	private Integer type;

}