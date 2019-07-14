package org.smartframework.cloud.examples.mall.service.product.entity.base;

import javax.persistence.Column;
import javax.persistence.Table;

import org.smartframework.cloud.starter.mybatis.common.mapper.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 商品信息
 *
 * @author liyulin
 * @date 2019-07-15
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(name = "t_product_info")
public class ProductInfoEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

    /** 商品名称 */
    @Column(name = "name")     
	private String name;
	
    /** 销售价格（单位：万分之一元） */
    @Column(name = "sellPrice")     
	private Long sellPrice;
	
    /** 库存 */
    @Column(name = "stock")     
	private Long stock;
	
	/** 表字段名 */
	public enum Columns {
	    /** 商品名称 */
		name,
	    /** 销售价格（单位：万分之一元） */
		sellPrice,
	    /** 库存 */
		stock;
	}

}