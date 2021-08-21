use test_dynamic_datasource_auth;

CREATE TABLE `t_role_info` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_code` varchar(64) NOT NULL COMMENT '角色编码',
  `f_description` varchar(128) NOT NULL COMMENT '角色描述',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`) USING BTREE,
  UNIQUE KEY `uk_code` (`f_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';