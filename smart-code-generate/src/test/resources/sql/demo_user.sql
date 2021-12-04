SET MODE=MySQL;

CREATE TABLE `t_login_info` (
  `f_id` bigint(20) unsigned NOT NULL,
  `t_user_id` bigint(20) unsigned NOT NULL,
  `f_username` varchar(20) NOT NULL COMMENT '用户名',
  `f_password` char(45) NOT NULL COMMENT '密码（md5加盐处理）',
  `f_salt` char(16) NOT NULL COMMENT '16位盐值',
  `f_last_login_time` datetime DEFAULT NULL COMMENT '最近成功登录时间',
  `f_pwd_state` tinyint(1) unsigned NOT NULL COMMENT '密码状态=={"1":"未设置","2":"已设置"}',
  `f_user_state` tinyint(1) unsigned NOT NULL COMMENT '用户状态=={"1":"启用","2":"禁用"}',
  `f_sys_insert_time` datetime NOT NULL COMMENT '新增时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '修改者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '记录状态=={"1":"正常","2":"已删除"}',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `f_username` (`f_username`),
  UNIQUE KEY `t_user_id` (`t_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录信息';

CREATE TABLE `t_user_info` (
  `f_id` bigint(20) unsigned NOT NULL,
  `f_mobile` char(11) DEFAULT NULL COMMENT '手机号',
  `f_nick_name` varchar(45) DEFAULT NULL COMMENT '昵称',
  `f_real_name` varchar(45) DEFAULT NULL COMMENT '真实姓名',
  `f_sex` tinyint(1) unsigned DEFAULT NULL COMMENT '性别=={"1":"男","2":"女","3":"未知"}',
  `f_birthday` date DEFAULT NULL COMMENT '出生年月',
  `f_profile_image` varchar(255) DEFAULT NULL COMMENT '头像',
  `f_channel` tinyint(1) unsigned NOT NULL COMMENT '所在平台=={"1":"app","2":"web后台","3":"微信"}',
  `f_sys_insert_time` datetime NOT NULL COMMENT '新增时间',
  `f_sys_upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_sys_del_time` datetime DEFAULT NULL COMMENT '删除时间',
  `f_sys_insert_user` bigint(20) unsigned DEFAULT NULL COMMENT '新增者',
  `f_sys_upd_user` bigint(20) unsigned DEFAULT NULL COMMENT '修改者',
  `f_sys_del_user` bigint(20) unsigned DEFAULT NULL COMMENT '删除者',
  `f_sys_del_state` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '记录状态=={"1":"正常","2":"已删除"}',
  PRIMARY KEY (`f_id`),
  UNIQUE KEY `f_mobile` (`f_mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';