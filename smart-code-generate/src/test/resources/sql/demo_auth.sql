SET MODE=MySQL;

CREATE TABLE `t_permission_info` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_code` varchar(32) NOT NULL COMMENT '权限编码',
  `f_description` varchar(64) NOT NULL COMMENT '权限描述',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`) ,
  UNIQUE KEY `uk_code1` (`f_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

CREATE TABLE `t_role_info` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_code` varchar(32) NOT NULL COMMENT '角色编码',
  `f_description` varchar(128) NOT NULL COMMENT '角色描述',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `uk_code2` (`f_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `t_role_permission_rela` (
  `f_id` bigint(20) unsigned NOT NULL,
  `t_role_info_id` bigint(20) unsigned NOT NULL COMMENT 't_role_info表id',
  `t_permission_info_id` bigint(20) unsigned NOT NULL COMMENT 't_permission_info表id',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`) ,
  KEY `idx_role_info_id1` (`t_role_info_id`),
  KEY `idx_permission_info_id` (`t_permission_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';

CREATE TABLE `t_user_role_rela` (
  `f_id` bigint(20) unsigned NOT NULL,
  `t_user_info_id` bigint(20) unsigned NOT NULL COMMENT 'demo_user库t_user_info表id',
  `t_role_info_id` bigint(20) unsigned NOT NULL COMMENT 't_role_info表id',
  `f_sys_insert_time` datetime NOT NULL COMMENT '创建时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '更新者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '删除状态=={1:正常, 2:已删除}',
  PRIMARY KEY (`f_id`) ,
  KEY `idx_user_info_id` (`t_user_info_id`),
  KEY `idx_role_info_id2` (`t_role_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';