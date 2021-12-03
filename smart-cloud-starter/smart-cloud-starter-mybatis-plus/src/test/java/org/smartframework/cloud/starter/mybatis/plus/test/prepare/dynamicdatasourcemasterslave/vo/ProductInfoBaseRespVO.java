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
package org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasourcemasterslave.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.BaseEntityResponse;

/**
 * 商品信息
 *
 * @author liyulin
 * @date 2021-03-14
 */
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class ProductInfoBaseRespVO extends BaseEntityResponse {

	private static final long serialVersionUID = 1L;

    /** 商品名称 */
	private String name;
	
    /** 销售价格（单位：万分之一元） */
	private Long sellPrice;
	
    /** 库存 */
	private Long stock;
	
}