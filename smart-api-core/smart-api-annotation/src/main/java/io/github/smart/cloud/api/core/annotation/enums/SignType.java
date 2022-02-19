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
package io.github.smart.cloud.api.core.annotation.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc 签名类型
 * @author collin
 * @date 2020/04/13
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SignType {

	/** 不需要签名 */
	NONE((byte) 1),
	/** 只有请求参数需要验签 */
	REQUEST((byte) 2),
	/** 只有响应参数需要签名 */
	RESPONSE((byte) 3),
	/** 请求参数需要验签，且响应参数需要签名 */
	ALL((byte) 4);

	/** 签名类型 */
	private byte type;
}