/*
Navicat MySQL Data Transfer

Source Server         : sloa_sd_uat
Source Server Version : 50720
Source Host           : 192.168.64.143:3306
Source Database       : sloa

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-01-11 10:53:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for attachment_item_tbl
-- ----------------------------
DROP TABLE IF EXISTS `attachment_item_tbl`;
CREATE TABLE `attachment_item_tbl` (
  `item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '附件文件ID',
  `mail_id` bigint(20) DEFAULT NULL COMMENT '传阅id',
  `bulk_id` varchar(128) DEFAULT NULL COMMENT '用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null',
  `user_id` bigint(20) DEFAULT NULL COMMENT '上传这个附件的传阅对象的ID',
  `creator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `create_time` datetime NOT NULL COMMENT '创建附件的时间',
  `file_name` varchar(128) NOT NULL COMMENT '文件上传时候的原名',
  `file_category` varchar(32) NOT NULL COMMENT '同一个实体可能有用不同用途的文件附件。开发人员自定义\n            默认值为空字符串。',
  `save_name` varchar(64) NOT NULL COMMENT '由uuid+类型后缀 组成',
  `url_path` varchar(256) NOT NULL COMMENT '出去服务器地址和webapp后的URL地址，c:url可用的地址。',
  `attached` bit(1) NOT NULL DEFAULT b'0' COMMENT '和实体绑定前，为false，绑定后为true\n            没有和实体绑定的附件，创建24小时之后都应该被删除',
  `state` int(5) DEFAULT NULL COMMENT '0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)',
  `item_size` varchar(50) DEFAULT NULL COMMENT '存放附件的大小, ',
  `item_neid` bigint(20) DEFAULT NULL COMMENT '存放文件上传到网盘的ID',
  `item_rev` varchar(50) DEFAULT NULL COMMENT '存放文件上传到网盘的文件版本',
  `item_differentiate` int(10) DEFAULT '0' COMMENT '区别附件存放在那个位置: 个人  还是  企业 (默认是个人)',
  `localhost_url_path` varchar(255) DEFAULT NULL COMMENT 'å­æ¾æ¬å°æä»¶çè®¿é®è·¯å¾',
  `update_time` datetime DEFAULT NULL COMMENT 'æ´æ°æ¶é´ï¼ä¸»è¦ç¨äºä¸è½½æä»¶',
  PRIMARY KEY (`item_id`),
  UNIQUE KEY `item_id` (`item_id`) USING BTREE,
  KEY `FK_Reference_12` (`mail_id`),
  KEY `bulk` (`bulk_id`) USING BTREE,
  KEY `item_user` (`user_id`) USING BTREE,
  KEY `ne` (`item_neid`) USING BTREE,
  KEY `diff` (`item_differentiate`) USING BTREE,
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`mail_id`) REFERENCES `mail_tbl` (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8 COMMENT='每个记录代表一个附件文件';

-- ----------------------------
-- Table structure for dictionary_tbl
-- ----------------------------
DROP TABLE IF EXISTS `dictionary_tbl`;
CREATE TABLE `dictionary_tbl` (
  `dic_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(64) NOT NULL,
  `key_value` varchar(512) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` bit(1) NOT NULL COMMENT '0 为失效 （false），1 为正常（true）',
  `description` varchar(512) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`dic_id`),
  KEY `Index_2` (`key_name`),
  KEY `Index_3` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for discuss_tbl
-- ----------------------------
DROP TABLE IF EXISTS `discuss_tbl`;
CREATE TABLE `discuss_tbl` (
  `discuss_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '讨论id',
  `mail_id` bigint(20) DEFAULT NULL COMMENT '传阅id',
  `create_time` datetime DEFAULT NULL COMMENT '发布的时间',
  `discuss_content` varchar(2000) NOT NULL COMMENT '讨论的内容',
  `user_id` bigint(20) NOT NULL COMMENT '讨论人id',
  `work_code` varchar(50) DEFAULT NULL COMMENT '讨论人工作编号',
  `last_name` varchar(50) DEFAULT NULL COMMENT '讨论人的姓名',
  `login_id` varchar(50) DEFAULT NULL COMMENT '用户的登录名称(冗余字段)',
  `head_portrait_url` varchar(50) DEFAULT NULL COMMENT '讨论人的头像URL',
  `differentiate` varchar(50) DEFAULT NULL COMMENT '区别是: 已发传阅人评论的记录或是已收传阅人的评论记录',
  PRIMARY KEY (`discuss_id`),
  UNIQUE KEY `diss` (`discuss_id`) USING BTREE,
  KEY `FK_Reference_10` (`mail_id`),
  KEY `di_us` (`user_id`) USING BTREE,
  KEY `diff_dis` (`differentiate`) USING BTREE,
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`mail_id`) REFERENCES `mail_tbl` (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hrmdepartment
-- ----------------------------
DROP TABLE IF EXISTS `hrmdepartment`;
CREATE TABLE `hrmdepartment` (
  `id` int(11) NOT NULL,
  `departmentmark` varchar(60) DEFAULT NULL,
  `departmentname` varchar(200) DEFAULT NULL,
  `subcompanyid1` int(11) DEFAULT NULL,
  `supdepid` int(11) DEFAULT NULL,
  `allsupdepid` text,
  `showorder` int(11) DEFAULT NULL,
  `canceled` char(1) DEFAULT NULL,
  `departmentcode` text,
  `coadjutant` int(11) DEFAULT NULL,
  `zzjgbmfzr` text,
  `zzjgbmfgld` text,
  `jzglbmfzr` text,
  `jzglbmfgld` text,
  `bmfzr` text,
  `bmfgld` text,
  `outkey` varchar(100) DEFAULT NULL,
  `budgetatuomoveorder` int(11) DEFAULT NULL,
  `ecology_pinyin_search` text,
  `tlevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hrmsubcompany
-- ----------------------------
DROP TABLE IF EXISTS `hrmsubcompany`;
CREATE TABLE `hrmsubcompany` (
  `id` int(11) NOT NULL,
  `subcompanyname` varchar(200) DEFAULT NULL,
  `subcompanydesc` varchar(200) DEFAULT NULL,
  `companyid` int(11) DEFAULT NULL,
  `supsubcomid` int(11) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `showorder` int(11) DEFAULT NULL,
  `canceled` char(1) DEFAULT NULL,
  `subcompanycode` varchar(100) DEFAULT NULL,
  `outkey` varchar(100) DEFAULT NULL,
  `budgetatuomoveorder` int(11) DEFAULT NULL,
  `ecology_pinyin_search` text,
  `limitusers` int(11) DEFAULT NULL,
  `tlevel` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for label_tbl
-- ----------------------------
DROP TABLE IF EXISTS `label_tbl`;
CREATE TABLE `label_tbl` (
  `label_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upload_type` varchar(50) DEFAULT NULL,
  `storage_type` varchar(50) DEFAULT NULL,
  `self_name` varchar(50) DEFAULT NULL,
  `company_name` varchar(50) DEFAULT NULL,
  `label_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`label_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mail_tbl
-- ----------------------------
DROP TABLE IF EXISTS `mail_tbl`;
CREATE TABLE `mail_tbl` (
  `mail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '传阅id',
  `user_id` bigint(20) NOT NULL COMMENT '发件人id',
  `work_code` varchar(50) DEFAULT NULL COMMENT '发件人工作编号',
  `last_name` varchar(50) DEFAULT NULL COMMENT '发件人的姓名',
  `login_id` varchar(50) DEFAULT NULL COMMENT '用户的登录名称(冗余字段)',
  `subcompany_name` varchar(50) DEFAULT NULL COMMENT '发件人的分部全称',
  `department_name` varchar(50) DEFAULT NULL COMMENT '发件人的部门全称',
  `all_receive_name` longtext COMMENT '可以有多个(冗余字段)',
  `title` longtext NOT NULL COMMENT '传阅主题(不做限制)',
  `mail_content` longtext NOT NULL COMMENT '邮件内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建传阅的时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送传阅的时间',
  `status` int(5) DEFAULT '0' COMMENT '0 无状态   1 待发传阅  7 已删除',
  `step_status` int(5) DEFAULT NULL COMMENT '1 发阅中   3 已完成',
  `complete_time` datetime DEFAULT NULL COMMENT '传阅完成的时间',
  `if_important` bit(1) DEFAULT b'0' COMMENT '重要传阅',
  `if_update` bit(1) DEFAULT b'0' COMMENT '允许修订附件',
  `if_upload` bit(1) DEFAULT b'0' COMMENT '允许上传附件',
  `if_read` bit(1) DEFAULT b'0' COMMENT '开封已阅确认',
  `if_notify` bit(1) DEFAULT b'0' COMMENT '短信提醒',
  `if_remind` bit(1) DEFAULT b'0' COMMENT '确认时提醒',
  `if_remind_all` bit(1) DEFAULT b'0' COMMENT '确认时提醒所有传阅对象',
  `if_secrecy` bit(1) DEFAULT b'0' COMMENT '传阅密送(不用实现)',
  `if_add` bit(1) DEFAULT b'0' COMMENT '允许新添加人员',
  `if_sequence` bit(1) DEFAULT b'0' COMMENT '有序确认(留个字段,不实现)',
  `has_attachment` bit(1) DEFAULT b'0' COMMENT '0 为没有 （false），1 为存在（true）',
  `enabled` bit(1) NOT NULL DEFAULT b'0' COMMENT '0 为未删除（false），1 为 删除（true）',
  `attention` bit(1) DEFAULT b'0' COMMENT '0 为未关注 （false），1 已关注（true）',
  `rule_name` varchar(185) DEFAULT NULL COMMENT '该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`mail_id`),
  UNIQUE KEY `mailId` (`mail_id`) USING BTREE,
  KEY `m_us` (`user_id`) USING BTREE,
  KEY `m_log` (`login_id`) USING BTREE,
  KEY `m_send` (`send_time`) USING BTREE,
  KEY `m_sta` (`status`) USING BTREE,
  KEY `sp` (`step_status`) USING BTREE,
  KEY `mail_at` (`attention`) USING BTREE,
  KEY `del_time` (`delete_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for menu_tbl
-- ----------------------------
DROP TABLE IF EXISTS `menu_tbl`;
CREATE TABLE `menu_tbl` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` datetime NOT NULL,
  PRIMARY KEY (`series`),
  KEY `Index_1` (`username`),
  KEY `Index_2` (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for receive_tbl
-- ----------------------------
DROP TABLE IF EXISTS `receive_tbl`;
CREATE TABLE `receive_tbl` (
  `receive_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收件id',
  `mail_id` bigint(20) DEFAULT NULL COMMENT '传阅id',
  `user_id` bigint(20) NOT NULL COMMENT '收件人id',
  `work_code` varchar(50) DEFAULT NULL COMMENT '收件人工作编号',
  `last_name` varchar(50) DEFAULT NULL COMMENT '收件人的姓名',
  `login_id` varchar(50) DEFAULT NULL COMMENT '用户的登录名称(冗余字段)',
  `subcompany_name` varchar(50) DEFAULT NULL COMMENT '收件人的分部全称',
  `department_name` varchar(50) DEFAULT NULL COMMENT '收件人的部门全称',
  `receive_time` datetime DEFAULT NULL COMMENT '接收传阅的时间',
  `join_time` datetime DEFAULT NULL COMMENT '添加联系人的时间',
  `receive_status` int(5) NOT NULL DEFAULT '0' COMMENT '状态: 0 未开封  1 已开封  ',
  `mail_state` int(5) DEFAULT NULL COMMENT '4 草稿  5 未读 6 已读  ',
  `step_status` int(5) DEFAULT NULL COMMENT '(冗余字段) 1 发阅中 2 待办传阅 3 已完成',
  `open_time` datetime DEFAULT NULL COMMENT '记录打开传阅的时间',
  `if_confirm` bit(1) DEFAULT b'0' COMMENT 'false 未确认 true 已确认 传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封',
  `affirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注',
  `confirm_record` varchar(150) DEFAULT NULL COMMENT '该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 ',
  `serial_num` int(11) DEFAULT '0' COMMENT '序号',
  `afresh_confim` bit(1) DEFAULT b'0' COMMENT '该字段用于重新确认传阅, false 未重新确认 true 重新确认 ',
  `ac_record` longtext COMMENT '该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 ',
  `afresh_remark` varchar(100) DEFAULT NULL COMMENT '当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)',
  `mh_time` datetime DEFAULT NULL COMMENT '确认时间(重新)',
  `receive_attention` bit(1) DEFAULT b'0' COMMENT '收件人的关注状态:  0 为未关注 （false），1 已关注（true）',
  `re_differentiate` bigint(20) DEFAULT '0' COMMENT '区别是谁添加的联系人, 存放添加该联系人的用户ID',
  PRIMARY KEY (`receive_id`),
  KEY `FK_Reference_11` (`mail_id`),
  KEY `re_us` (`user_id`) USING BTREE,
  KEY `re_log` (`login_id`) USING BTREE,
  KEY `re_st` (`receive_status`) USING BTREE,
  KEY `re_ms` (`mail_state`) USING BTREE,
  KEY `re_ss` (`step_status`) USING BTREE,
  KEY `re_at` (`receive_attention`) USING BTREE,
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`mail_id`) REFERENCES `mail_tbl` (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3335 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_menu_tbl
-- ----------------------------
DROP TABLE IF EXISTS `role_menu_tbl`;
CREATE TABLE `role_menu_tbl` (
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`),
  KEY `FK_Reference_9` (`menu_id`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`role_id`) REFERENCES `role_tbl` (`role_id`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`menu_id`) REFERENCES `menu_tbl` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role_tbl
-- ----------------------------
DROP TABLE IF EXISTS `role_tbl`;
CREATE TABLE `role_tbl` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `role_name` varchar(32) NOT NULL,
  `role_description` varchar(256) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL COMMENT '0 为失效 （false），1 为正常（true）',
  PRIMARY KEY (`role_id`),
  KEY `FK_Reference_6` (`parent_id`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`parent_id`) REFERENCES `role_tbl` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_backup_log_tbl
-- ----------------------------
DROP TABLE IF EXISTS `system_backup_log_tbl`;
CREATE TABLE `system_backup_log_tbl` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `log_time` datetime NOT NULL,
  `action` longtext NOT NULL,
  `ip` varchar(32) NOT NULL,
  `identity_info` varchar(64) DEFAULT NULL COMMENT '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。   不和用户表直接外键关联。',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_log_tbl
-- ----------------------------
DROP TABLE IF EXISTS `system_log_tbl`;
CREATE TABLE `system_log_tbl` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator` varchar(64) DEFAULT NULL COMMENT '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
  `log_time` datetime NOT NULL,
  `action` longtext NOT NULL,
  `ip` varchar(32) NOT NULL,
  `identity_info` varchar(64) DEFAULT NULL COMMENT '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。   不和用户表直接外键关联。',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tag_tbl
-- ----------------------------
DROP TABLE IF EXISTS `tag_tbl`;
CREATE TABLE `tag_tbl` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_type` varchar(50) DEFAULT NULL COMMENT '上传文件的类型',
  `storage_type` varchar(50) DEFAULT NULL COMMENT '存放文件的方式, 网盘还是本地',
  `self_name` varchar(50) DEFAULT NULL COMMENT '自定义的标识',
  `company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `label_status` int(5) DEFAULT '0' COMMENT '0 正常  1 废弃不用',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for update_record_tbl
-- ----------------------------
DROP TABLE IF EXISTS `update_record_tbl`;
CREATE TABLE `update_record_tbl` (
  `update_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '修改ID',
  `item_id` bigint(20) DEFAULT NULL COMMENT '附件文件ID',
  `receive_id` bigint(20) DEFAULT NULL COMMENT '收件id',
  `update_time` datetime DEFAULT NULL COMMENT '修改的时间',
  `update_action` int(5) DEFAULT NULL COMMENT '0 为新增 ; 1 为修改 ; 2 为删除',
  PRIMARY KEY (`update_id`),
  KEY `FK_Reference_13` (`receive_id`),
  KEY `FK_Reference_15` (`item_id`),
  CONSTRAINT `FK_Reference_13` FOREIGN KEY (`receive_id`) REFERENCES `receive_tbl` (`receive_id`),
  CONSTRAINT `FK_Reference_15` FOREIGN KEY (`item_id`) REFERENCES `attachment_item_tbl` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_collection_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_collection_tbl`;
CREATE TABLE `user_collection_tbl` (
  `collection_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `mail_id` bigint(20) DEFAULT NULL COMMENT '传阅id',
  `user_id` bigint(20) NOT NULL COMMENT '收藏人id',
  `work_code` varchar(50) DEFAULT NULL COMMENT '收藏人工作编号',
  `last_name` varchar(50) DEFAULT NULL COMMENT '收藏人的姓名',
  `collection_time` datetime DEFAULT NULL COMMENT '收藏时间',
  `differentiate` varchar(50) DEFAULT NULL COMMENT '区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录',
  PRIMARY KEY (`collection_id`),
  KEY `FK_Reference_14` (`mail_id`),
  KEY `uc_us` (`user_id`) USING BTREE,
  KEY `uc_diff` (`differentiate`) USING BTREE,
  KEY `uc_co` (`collection_time`) USING BTREE,
  CONSTRAINT `FK_Reference_14` FOREIGN KEY (`mail_id`) REFERENCES `mail_tbl` (`mail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_info_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_info_tbl`;
CREATE TABLE `user_info_tbl` (
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

-- ----------------------------
-- Table structure for user_mssage_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_mssage_tbl`;
CREATE TABLE `user_mssage_tbl` (
  `user_mssage_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户信息ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `work_code` varchar(50) DEFAULT NULL COMMENT '用户工作编号',
  `last_name` varchar(50) DEFAULT NULL COMMENT '用户的姓名',
  `login_id` varchar(50) DEFAULT NULL COMMENT '系统账号 登录名',
  `dept_fullname` varchar(50) DEFAULT NULL COMMENT '部门全称',
  `full_name` varchar(50) DEFAULT NULL COMMENT '分部全称',
  `department_id` varchar(50) DEFAULT NULL COMMENT '用户部门ID',
  `subcompany_id1` varchar(50) DEFAULT NULL COMMENT '用户分部ID',
  `status` varchar(50) DEFAULT NULL,
  `dsporder` float DEFAULT NULL,
  `box_session` varchar(64) DEFAULT NULL COMMENT '网盘session',
  `mobile` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_mssage_id`),
  UNIQUE KEY `um_us` (`user_id`) USING BTREE,
  KEY `um_log` (`login_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=896123 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_role_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_role_tbl`;
CREATE TABLE `user_role_tbl` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_Reference_2` (`role_id`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`user_id`) REFERENCES `user_tbl` (`user_id`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`role_id`) REFERENCES `role_tbl` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_tbl
-- ----------------------------
DROP TABLE IF EXISTS `user_tbl`;
CREATE TABLE `user_tbl` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(32) NOT NULL,
  `nick_name` varchar(64) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `user_password` varchar(128) NOT NULL,
  `admin` bit(1) NOT NULL DEFAULT b'0',
  `enabled` bit(1) NOT NULL COMMENT '0 为失效 false，1 为正常 true',
  `last_login` datetime DEFAULT NULL,
  `priority` int(4) NOT NULL,
  `soft_delete` bit(1) NOT NULL DEFAULT b'0',
  `access_token` varchar(128) DEFAULT NULL,
  `access_token_expire` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
