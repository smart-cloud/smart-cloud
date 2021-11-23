CREATE TABLE `t_rpc_log` (
  `f_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `f_api_desc` varchar(64) DEFAULT NULL COMMENT '接口描述',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口日志记录';


CREATE TABLE `t_order_bill_0` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx0_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_1` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx1_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_2` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx2_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_3` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx3_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_4` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx4_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_5` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx5_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_6` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx6_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_7` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx7_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_8` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx8_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_9` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx9_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_10` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx10_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_11` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx11_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_12` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx12_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_13` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx13_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_14` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx14_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_15` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx15_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_16` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx16_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_17` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx17_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_18` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx18_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `t_order_bill_19` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_order_no` varchar(32) NOT NULL COMMENT '订单号',
  `f_amount` bigint(20) unsigned NOT NULL COMMENT '订单金额总金额',
  `f_status` tinyint(2) unsigned NOT NULL COMMENT '订单状态（1：待扣减库存；2：扣减库存失败；3：抵扣优惠券失败；4：待付款；5：已取消；6：待发货；7：待收货；8：待评价，9：已完成）',
  `f_pay_state` tinyint(1) unsigned NOT NULL COMMENT '支付状态（1：待支付；2：支付成功；3：支付失败；4：待退款；5：退款成功；6：退款失败）',
  `f_buyer` bigint(20) unsigned NOT NULL COMMENT '购买人id（demo_user库t_user_info表f_id）',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  KEY `idx19_order_no` (`f_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';