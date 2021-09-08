package org.smartframework.cloud.starter.mp.shardingjdbc.test.prepare.shardingjdbcshardingmasterslave.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.starter.mybatis.plus.common.mapper.entity.BaseEntity;

/**
 * 订单信息
 *
 * @author liyulin
 * @date 2021-03-23
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@TableName("t_order_bill")
public class OrderBillEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

    /** 订单号 */
    @TableField(value = "f_order_no")
	private String orderNo;
	
    /** 订单金额总金额 */
    @TableField(value = "f_amount")
	private Long amount;
	
    /** 订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成） */
    @TableField(value = "f_status")
	private Byte status;
	
    /** 支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败） */
    @TableField(value = "f_pay_state")
	private Byte payState;
	
    /** 购买人id（demo_user库t_user_info表f_id） */
    @TableField(value = "f_buyer")
	private Long buyer;
	
}