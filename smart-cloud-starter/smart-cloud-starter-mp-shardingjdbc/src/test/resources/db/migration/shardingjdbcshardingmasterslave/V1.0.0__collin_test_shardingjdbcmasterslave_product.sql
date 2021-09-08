CREATE TABLE `t_product_info_0`  (
  `f_id` bigint(20) UNSIGNED NOT NULL,
  `f_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `f_sell_price` bigint(20) UNSIGNED NOT NULL COMMENT '销售价格（单位：万分之一元）',
  `f_stock` bigint(20) UNSIGNED NOT NULL COMMENT '库存',
  `f_sys_insert_time` datetime(0) NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime(0) NULL DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品信息' ROW_FORMAT = Dynamic;

CREATE TABLE t_product_info_1 LIKE t_product_info_0;
CREATE TABLE t_product_info_2 LIKE t_product_info_0;
CREATE TABLE t_product_info_3 LIKE t_product_info_0;
CREATE TABLE t_product_info_4 LIKE t_product_info_0;
CREATE TABLE t_product_info_5 LIKE t_product_info_0;
CREATE TABLE t_product_info_6 LIKE t_product_info_0;
CREATE TABLE t_product_info_7 LIKE t_product_info_0;
CREATE TABLE t_product_info_8 LIKE t_product_info_0;
CREATE TABLE t_product_info_9 LIKE t_product_info_0;
CREATE TABLE t_product_info_10 LIKE t_product_info_0;
CREATE TABLE t_product_info_11 LIKE t_product_info_0;
CREATE TABLE t_product_info_12 LIKE t_product_info_0;
CREATE TABLE t_product_info_13 LIKE t_product_info_0;
CREATE TABLE t_product_info_14 LIKE t_product_info_0;
CREATE TABLE t_product_info_15 LIKE t_product_info_0;
CREATE TABLE t_product_info_16 LIKE t_product_info_0;
CREATE TABLE t_product_info_17 LIKE t_product_info_0;
CREATE TABLE t_product_info_18 LIKE t_product_info_0;
CREATE TABLE t_product_info_19 LIKE t_product_info_0;