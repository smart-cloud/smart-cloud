package org.smartframework.cloud.starter.mybatis.plus.test.prepare.dynamicdatasource.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.entity.BaseEntity;

/**
 * 商品信息
 *
 * @author liyulin
 * @date 2021-03-14
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TableName("t_product_info")
public class ProductInfoEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

    /** 商品名称 */
    @TableField(value = "f_name")
	private String name;
	
    /** 销售价格（单位：万分之一元） */
    @TableField(value = "f_sell_price")
	private Long sellPrice;
	
    /** 库存 */
    @TableField(value = "f_stock")
	private Long stock;
	
}