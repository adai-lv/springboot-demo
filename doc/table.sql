-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '代理键',
  `username` varchar(45) NOT NULL COMMENT '用户名，英文+数字的组合',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码，加密储存，不可解密',
  `nickname` varchar(45) NOT NULL DEFAULT '' COMMENT '昵称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态，0正常，1未激活，2已锁定，3永久删除',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账号信息';

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '代理键',
  `account_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '账号ID',
  `realname` varchar(45) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `birtyday` date NULL COMMENT '生日，格式：yyyy-mm-dd',
  `gender` char(1) NOT NULL DEFAULT 'M' COMMENT '性别，M男性，F女性',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户档案';