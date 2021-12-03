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
package org.smartframework.cloud.mask;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @desc mask规则
 * @author liyulin
 * @date 2019/11/05
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MaskRule {

	/** 手机号 */
	MOBILE(3, 4, DefaultMaskConfig.MASK_TEXT),
	/** 银行卡 */
	BANKCARD(4, 4, DefaultMaskConfig.MASK_TEXT),
	/** 身份证 */
	IDCARD(4, 4, DefaultMaskConfig.MASK_TEXT),
	/** ip地址 */
	IP(3, 0, DefaultMaskConfig.MASK_TEXT),
	/** 名字 */
	NAME(1, 0, DefaultMaskConfig.MASK_TEXT),
	/** 密码 */
	PASSWROD(0, 0, DefaultMaskConfig.MASK_TEXT),
	/** 默认（全部mask） */
	DEFAULT(0, 0, DefaultMaskConfig.MASK_TEXT);

	/** 开头保留的长度 */
	private int startLen;
	/** 结尾保留的长度 */
	private int endLen;
	/** 掩码值 */
	private String mask;

}