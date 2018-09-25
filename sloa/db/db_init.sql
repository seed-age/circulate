-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.1.19-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 mybox.attachment_item_tbl 结构
CREATE TABLE IF NOT EXISTS `attachment_item_tbl` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entity_id` varchar(128) DEFAULT NULL COMMENT '类名和ID，冒号作为连接符\r\n            对于没有和实体绑定的临时附件，这个值为NULL。',
  `bulk_id` varchar(128) DEFAULT NULL COMMENT '用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null',
  `creator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `create_time` datetime NOT NULL,
  `file_name` varchar(128) NOT NULL COMMENT '文件上传时候的原名',
  `file_category` varchar(32) NOT NULL COMMENT '同一个实体可能有用不同用途的文件附件。开发人员自定义\r\n            默认值为空字符串。',
  `save_name` varchar(64) NOT NULL COMMENT '由uuid+类型后缀 组成',
  `save_dir` varchar(256) NOT NULL COMMENT '相对路径\r\n            相对于所属FileStore的根目录',
  `url_path` varchar(256) NOT NULL COMMENT '出去服务器地址和webapp后的URL地址，c:url可用的地址。',
  `attached` bit(1) NOT NULL DEFAULT b'0' COMMENT '和实体绑定前，为false，绑定后为true\r\n            没有和实体绑定的附件，创建24小时之后都应该被删除',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 正在导出表  mybox.attachment_item_tbl 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `attachment_item_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment_item_tbl` ENABLE KEYS */;

-- 导出  表 mybox.dictionary_tbl 结构
CREATE TABLE IF NOT EXISTS `dictionary_tbl` (
  `dic_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(32) NOT NULL,
  `key_value` varchar(64) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` bit(1) NOT NULL COMMENT '0 为失效 （false），1 为正常（true）',
  `description` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`dic_id`),
  KEY `Index_2` (`key_name`) USING BTREE,
  KEY `Index_3` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.dictionary_tbl 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `dictionary_tbl` DISABLE KEYS */;
INSERT INTO `dictionary_tbl` (`dic_id`, `key_name`, `key_value`, `update_time`, `create_time`, `status`, `description`) VALUES
	(2, 'defaultPageSize', '25', '2012-08-09 18:13:37', '2012-08-09 18:13:37', b'1', NULL),
	(4, 'defaultPageSize:1', '30', '2013-11-26 17:59:45', '2013-11-26 17:59:45', b'1', NULL);
/*!40000 ALTER TABLE `dictionary_tbl` ENABLE KEYS */;

-- 导出  表 mybox.menu_tbl 结构
CREATE TABLE IF NOT EXISTS `menu_tbl` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_menu_id` bigint(12) DEFAULT NULL,
  `menu_text` varchar(32) NOT NULL,
  `icon` varchar(128) DEFAULT NULL COMMENT '图标class和图标路径只能填写其中一种。',
  `action_path` varchar(256) DEFAULT NULL,
  `ext_id` varchar(64) DEFAULT NULL,
  `layout` varchar(256) DEFAULT NULL,
  `leaf` bit(1) NOT NULL,
  `expanded` bit(1) NOT NULL,
  `index_value` int(3) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `icon_cls` varchar(32) DEFAULT NULL COMMENT '图标class和图标路径只能填写其中一种。',
  PRIMARY KEY (`menu_id`),
  KEY `FK_Reference_7` (`parent_menu_id`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`parent_menu_id`) REFERENCES `menu_tbl` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.menu_tbl 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `menu_tbl` DISABLE KEYS */;
INSERT INTO `menu_tbl` (`menu_id`, `parent_menu_id`, `menu_text`, `icon`, `action_path`, `ext_id`, `layout`, `leaf`, `expanded`, `index_value`, `enabled`, `icon_cls`) VALUES
	(1, NULL, '系统管理', '../extjs/icons/img/system.png', ' ', '', '', b'0', b'1', 0, b'1', NULL),
	(3, 1, '用户管理', '../extjs/icons/img/user.png', 'users/users-grid.htm', '', '', b'1', b'0', 1, b'1', NULL),
	(4, 1, '菜单管理', '../extjs/icons/img/manage.png', 'menus/menus-setting.htm', '', '', b'1', b'0', 0, b'1', NULL),
	(16, 1, '角色管理', NULL, 'roles/roles-setting.htm', '', '', b'1', b'0', 2, b'1', 'icon-group'),
	(17, 1, '日志管理', '../extjs/icons/img/script_error.png', 'log/log-grid.htm', '', '', b'1', b'0', 3, b'1', NULL);
/*!40000 ALTER TABLE `menu_tbl` ENABLE KEYS */;

-- 导出  表 mybox.persistent_logins 结构
CREATE TABLE IF NOT EXISTS `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` datetime NOT NULL,
  PRIMARY KEY (`series`),
  KEY `Index_1` (`username`) USING BTREE,
  KEY `Index_2` (`series`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 正在导出表  mybox.persistent_logins 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `persistent_logins` DISABLE KEYS */;
/*!40000 ALTER TABLE `persistent_logins` ENABLE KEYS */;

-- 导出  表 mybox.role_menu_tbl 结构
CREATE TABLE IF NOT EXISTS `role_menu_tbl` (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `FK_Reference_9` (`menu_id`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`role_id`) REFERENCES `role_tbl` (`role_id`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`menu_id`) REFERENCES `menu_tbl` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.role_menu_tbl 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `role_menu_tbl` DISABLE KEYS */;
INSERT INTO `role_menu_tbl` (`role_id`, `menu_id`) VALUES
	(1, 1),
	(1, 3),
	(1, 4),
	(1, 16),
	(1, 17),
	(3, 1),
	(3, 3),
	(3, 16),
	(3, 17);
/*!40000 ALTER TABLE `role_menu_tbl` ENABLE KEYS */;

-- 导出  表 mybox.role_tbl 结构
CREATE TABLE IF NOT EXISTS `role_tbl` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `role_name` varchar(32) NOT NULL,
  `role_description` varchar(256) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL COMMENT '0 为失效 （false），1 为正常（true）',
  PRIMARY KEY (`role_id`),
  KEY `FK_Reference_6` (`parent_id`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`parent_id`) REFERENCES `role_tbl` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.role_tbl 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `role_tbl` DISABLE KEYS */;
INSERT INTO `role_tbl` (`role_id`, `parent_id`, `role_name`, `role_description`, `status`) VALUES
	(1, NULL, '系统管理员', '系统管理员，可以进行系统管理', b'1'),
	(2, NULL, '运维人员', '测试角色', b'1'),
	(3, NULL, '开发人员', '测试角色', b'1');
/*!40000 ALTER TABLE `role_tbl` ENABLE KEYS */;

-- 导出  表 mybox.system_backup_log_tbl 结构
CREATE TABLE IF NOT EXISTS `system_backup_log_tbl` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `log_time` datetime NOT NULL,
  `action` longtext NOT NULL,
  `ip` varchar(32) NOT NULL,
  `identity_info` varchar(64) DEFAULT NULL COMMENT '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。\r\n            不和用户表直接外键关联。',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 正在导出表  mybox.system_backup_log_tbl 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `system_backup_log_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_backup_log_tbl` ENABLE KEYS */;

-- 导出  表 mybox.system_log_tbl 结构
CREATE TABLE IF NOT EXISTS `system_log_tbl` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `log_time` datetime NOT NULL,
  `action` longtext NOT NULL,
  `ip` varchar(32) NOT NULL,
  `identity_info` varchar(64) DEFAULT NULL COMMENT '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。\r\n            不和用户表直接外键关联。',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 正在导出表  mybox.system_log_tbl 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `system_log_tbl` DISABLE KEYS */;
INSERT INTO `system_log_tbl` (`log_id`, `operator`, `log_time`, `action`, `ip`, `identity_info`) VALUES
	(1, '超级管理员', '2016-07-29 17:05:51', '删除账户 &lt;script&gt;中&lt;/script&gt; 账户详情: {"accountName":"test001","admin":true,"createTime":"2016-07-29 16:34:56","enabled":true,"nickName":"&lt;script&gt;中&lt;/script&gt;","priority":0,"softDelete":false,"userId":2,"userPassword":"81dc9bdb52d04dc20036dbd8313ed055"} ', '0:0:0:0:0:0:0:1', '超级管理员[admin],id:1'),
	(2, '超级管理员', '2016-07-29 17:07:15', '删除账户 &lt;script&gt;中&lt;script&gt; 账户详情: {"accountName":"test001","admin":true,"createTime":"2016-07-29 17:06:53","enabled":true,"nickName":"&lt;script&gt;中&lt;script&gt;","priority":0,"softDelete":false,"userId":3,"userPassword":"81dc9bdb52d04dc20036dbd8313ed055"} ', '0:0:0:0:0:0:0:1', '超级管理员[admin],id:1');
/*!40000 ALTER TABLE `system_log_tbl` ENABLE KEYS */;

-- 导出  表 mybox.user_info_tbl 结构
CREATE TABLE IF NOT EXISTS `user_info_tbl` (
  `user_id` bigint(20) NOT NULL,
  `last_name` varchar(32) DEFAULT NULL,
  `first_name` varchar(32) DEFAULT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `phone_number` varchar(32) DEFAULT NULL,
  `remark` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.user_info_tbl 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `user_info_tbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_info_tbl` ENABLE KEYS */;

-- 导出  表 mybox.user_role_tbl 结构
CREATE TABLE IF NOT EXISTS `user_role_tbl` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_Reference_2` (`role_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`user_id`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`role_id`) REFERENCES `role_tbl` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.user_role_tbl 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `user_role_tbl` DISABLE KEYS */;
INSERT INTO `user_role_tbl` (`user_id`, `role_id`) VALUES
	(1, 1),
	(1, 2),
	(1, 3),
	(2, 3);
/*!40000 ALTER TABLE `user_role_tbl` ENABLE KEYS */;

-- 导出  表 mybox.user_tbl 结构
CREATE TABLE IF NOT EXISTS `user_tbl` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(32) NOT NULL,
  `nick_name` varchar(64) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `user_password` varchar(128) NOT NULL,
  `admin` bit(1) NOT NULL DEFAULT b'0',
  `enabled` bit(1) NOT NULL COMMENT '0 为失效 （false），1 为正常（true）',
  `last_login` datetime DEFAULT NULL,
  `priority` int(4) NOT NULL,
  `soft_delete` bit(1) NOT NULL DEFAULT b'0',
  `access_token` varchar(128) DEFAULT NULL,
  `access_token_expire` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  mybox.user_tbl 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `user_tbl` DISABLE KEYS */;
INSERT INTO `user_tbl` (`user_id`, `account_name`, `nick_name`, `create_time`, `user_password`, `admin`, `enabled`, `last_login`, `priority`, `soft_delete`, `access_token`, `access_token_expire`) VALUES
	(1, 'admin', '超级管理员', '2012-07-27 11:02:34', '$2a$10$zON6EvX6yVFPXJAhcSNxPO87oN/8ZAWyg3Odgs75COERP0iy/Psse', b'1', b'1', '2017-08-02 11:56:11', 1, b'0', NULL, NULL),
	(2, 'test', '测试用户', '2017-03-02 10:50:45', '$2a$10$IbeioOQqfqrMT205Hdh4ce7FMRVKMZvZtJ42DtCbv3Fmu4ZMFtCn2', b'1', b'1', '2017-04-01 17:02:06', 0, b'0', NULL, NULL);
/*!40000 ALTER TABLE `user_tbl` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
