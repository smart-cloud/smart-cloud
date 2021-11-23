CREATE TABLE `t_product_info_0`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_1`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_2`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_3`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_4`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_5`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_6`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_7`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_8`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_9`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_10`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_11`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_12`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_13`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_14`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_15`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_16`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_17`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_18`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';

CREATE TABLE `t_product_info_19`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT = '商品信息';