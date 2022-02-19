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
package io.github.smart.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 公共信息
 *
 * @author collin
 * @date 2019-07-13
 */
@Getter
@Setter
@ToString
public class CommonBO {

	/** 包名 */
	private String packageName;
	/** 需要导入的包名 */
	private Set<String> importPackages;
	/** 类名 */
	private String className;
	/** 表备注 */
	private String tableComment;
	/** 类注释信息 */
	private ClassCommentBO classComment;

}