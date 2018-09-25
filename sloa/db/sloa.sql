/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/9/25 18:35:44                           */
/*==============================================================*/


drop table if exists attachment_item_tbl;

drop table if exists dictionary_tbl;

drop table if exists discuss_tbl;

drop table if exists hrmdepartment;

drop table if exists hrmsubcompany;

drop table if exists mail_tbl;

drop table if exists menu_tbl;

drop table if exists persistent_logins;

drop table if exists receive_tbl;

drop table if exists role_menu_tbl;

drop table if exists role_tbl;

drop table if exists system_backup_log_tbl;

drop table if exists system_log_tbl;

drop table if exists tag_tbl;

drop table if exists update_record_tbl;

drop table if exists user_collection_tbl;

drop table if exists user_info_tbl;

drop table if exists user_mssage_tbl;

drop table if exists user_role_tbl;

drop table if exists user_tbl;

/*==============================================================*/
/* Table: attachment_item_tbl                                   */
/*==============================================================*/
create table attachment_item_tbl
(
   item_id              bigint not null auto_increment comment '附件文件ID',
   mail_id              bigint comment '传阅id',
   bulk_id              varchar(128) comment '用该批次ID进行管理多个上次附件，当附件和实体绑定的时候，此批次ID会设置为null',
   user_id              bigint comment '上传这个附件的传阅对象的ID',
   creator              varchar(64) comment '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
   create_time          datetime not null comment '创建附件的时间',
   file_name            varchar(128) not null comment '文件上传时候的原名',
   file_category        varchar(32) not null comment '同一个实体可能有用不同用途的文件附件。开发人员自定义
            默认值为空字符串。',
   save_name            varchar(64) not null comment '由uuid+类型后缀 组成',
   url_path             varchar(256) not null comment '出去服务器地址和webapp后的URL地址，c:url可用的地址。',
   attached             bit(1) not null default 0 comment '和实体绑定前，为false，绑定后为true
            没有和实体绑定的附件，创建24小时之后都应该被删除',
   state                int(5) default 0 comment '0 为原始 ; 1 为修订 ; 2 为新增(表示增加了一个附件)',
   item_size            varchar(50) comment '存放附件的大小, ',
   item_neid            bigint comment '存放文件上传到网盘的ID',
   item_rev             varchar(50) comment '存放文件上传到网盘的文件版本',
   item_differentiate   int(10) default 0 comment '区别附件存放在那个位置: 个人  还是  企业 (默认是个人)',
   localhost_url_path   varchar(50) comment '存储APP端下载到本地的URL',
   update_time          datetime comment '更新时间(主要用于localhost_url_path的字段更新)',
   primary key (item_id)
);

alter table attachment_item_tbl comment '每个记录代表一个附件文件';

/*==============================================================*/
/* Table: dictionary_tbl                                        */
/*==============================================================*/
create table dictionary_tbl
(
   dic_id               bigint not null auto_increment,
   key_name             varchar(64) not null,
   key_value            varchar(512) not null,
   update_time          datetime,
   create_time          datetime,
   status               bit not null comment '0 为失效 （false），1 为正常（true）',
   description          varchar(512),
   type                 varchar(64),
   primary key (dic_id)
);

/*==============================================================*/
/* Index: Index_2                                               */
/*==============================================================*/
create index Index_2 on dictionary_tbl
(
   key_name
);

/*==============================================================*/
/* Index: Index_3                                               */
/*==============================================================*/
create index Index_3 on dictionary_tbl
(
   status
);

/*==============================================================*/
/* Table: discuss_tbl                                           */
/*==============================================================*/
create table discuss_tbl
(
   discuss_id           bigint not null auto_increment comment '讨论id',
   mail_id              bigint comment '传阅id',
   create_time          datetime comment '发布的时间',
   discuss_content      varchar(2000) not null comment '讨论的内容',
   user_id              bigint not null comment '讨论人id',
   work_code            varchar(50) comment '讨论人工作编号',
   last_name            varchar(50) comment '讨论人的姓名',
   login_id             varchar(50) comment '用户的登录名称(冗余字段)',
   head_portrait_url    varchar(50) comment '讨论人的头像URL',
   differentiate        varchar(50) comment '区别是: 已发传阅人评论的记录或是已收传阅人的评论记录',
   primary key (discuss_id)
);

/*==============================================================*/
/* Table: hrmdepartment                                         */
/*==============================================================*/
create table hrmdepartment
(
   id                   int(11) not null,
   departmentmark       varchar(60),
   departmentname       varchar(200),
   subcompanyid1        int(11),
   supdepid             int(11),
   allsupdepid          text,
   showorder            int(11),
   canceled             char(1),
   departmentcode       text,
   coadjutant           int(11),
   zzjgbmfzr            text,
   zzjgbmfgld           text,
   jzglbmfzr            text,
   jzglbmfgld           text,
   bmfzr                text,
   bmfgld               text,
   outkey               varchar(100),
   budgetatuomoveorder  int(11),
   ecology_pinyin_search text,
   tlevel               int(11),
   primary key (id)
);

/*==============================================================*/
/* Table: hrmsubcompany                                         */
/*==============================================================*/
create table hrmsubcompany
(
   id                   int(11) not null,
   subcompanyname       varchar(200),
   subcompanydesc       varchar(200),
   companyid            int(11),
   supsubcomid          int(11),
   url                  varchar(50),
   showorder            int(11),
   canceled             char(1),
   subcompanycode       varchar(100),
   outkey               varchar(100),
   budgetatuomoveorder  int(11),
   ecology_pinyin_search text,
   limitusers           int(11),
   tlevel               int(11),
   primary key (id)
);

/*==============================================================*/
/* Table: mail_tbl                                              */
/*==============================================================*/
create table mail_tbl
(
   mail_id              bigint not null auto_increment comment '传阅id',
   user_id              bigint not null comment '发件人id',
   work_code            varchar(50) comment '发件人工作编号',
   last_name            varchar(50) comment '发件人的姓名',
   login_id             varchar(50) comment '用户的登录名称(冗余字段)',
   subcompany_name      varchar(50) comment '发件人的分部全称',
   department_name      varchar(50) comment '发件人的部门全称',
   all_receive_name     longtext comment '可以有多个(冗余字段)',
   title                longtext not null comment '传阅主题(不做限制)',
   mail_content         longtext not null comment '邮件内容',
   create_time          datetime comment '创建传阅的时间',
   send_time            datetime comment '发送传阅的时间',
   status               int(5) default 0 comment '0 无状态   1 待发传阅  7 已删除',
   step_status          int(5) default 0 comment '1 发阅中   3 已完成',
   complete_time        datetime comment '传阅完成的时间',
   if_important         bit(1) default 0 comment '重要传阅',
   if_update            bit(1) default 0 comment '允许修订附件',
   if_upload            bit(1) default 0 comment '允许上传附件',
   if_read              bit(1) default 0 comment '开封已阅确认',
   if_notify            bit(1) default 0 comment '短信提醒',
   if_remind            bit(1) default 0 comment '确认时提醒',
   if_remind_all        bit(1) default 0 comment '确认时提醒所有传阅对象',
   if_secrecy           bit(1) default 0 comment '传阅密送(不用实现)',
   if_add               bit(1) default 0 comment '允许新添加人员',
   if_sequence          bit(1) default 0 comment '有序确认(留个字段,不实现)',
   has_attachment       bit(1) default 0 comment '0 为没有 （false），1 为存在（true）',
   enabled              bit(1) not null default 0 comment '0 为未删除（false），1 为 删除（true）',
   attention            bit(1) default 0 comment '0 为未关注 （false），1 已关注（true）',
   rule_name            varchar(185) comment '该字段用于记录选择的传阅规则的名字 ，已分号分隔 ；',
   delete_time          datetime comment '删除传阅的时间',
   primary key (mail_id)
);

/*==============================================================*/
/* Table: menu_tbl                                              */
/*==============================================================*/
create table menu_tbl
(
   menu_id              bigint not null auto_increment,
   parent_menu_id       bigint(12),
   menu_text            varchar(32) not null,
   icon                 varchar(128) comment '图标class和图标路径只能填写其中一种。',
   action_path          varchar(256),
   ext_id               varchar(64),
   layout               varchar(256),
   leaf                 bit(1) not null,
   expanded             bit(1) not null,
   index_value          int(3) not null,
   enabled              bit(1) not null,
   icon_cls             varchar(32) comment '图标class和图标路径只能填写其中一种。',
   primary key (menu_id)
);

/*==============================================================*/
/* Table: persistent_logins                                     */
/*==============================================================*/
create table persistent_logins
(
   series               varchar(64) not null,
   username             varchar(64) not null,
   token                varchar(64) not null,
   last_used            datetime not null,
   primary key (series)
);

/*==============================================================*/
/* Index: Index_1                                               */
/*==============================================================*/
create index Index_1 on persistent_logins
(
   username
);

/*==============================================================*/
/* Index: Index_2                                               */
/*==============================================================*/
create index Index_2 on persistent_logins
(
   series
);

/*==============================================================*/
/* Table: receive_tbl                                           */
/*==============================================================*/
create table receive_tbl
(
   receive_id           bigint not null auto_increment comment '收件id',
   mail_id              bigint comment '传阅id',
   user_id              bigint not null comment '收件人id',
   work_code            varchar(50) comment '收件人工作编号',
   last_name            varchar(50) comment '收件人的姓名',
   login_id             varchar(50) comment '用户的登录名称(冗余字段)',
   subcompany_name      varchar(50) comment '收件人的分部全称',
   department_name      varchar(50) comment '收件人的部门全称',
   receive_time         datetime comment '接收传阅的时间',
   join_time            datetime comment '添加联系人的时间',
   receive_status       int(5) not null default 0 comment '状态: 0 未开封  1 已开封  ',
   mail_state           int(5) comment '4 草稿  5 未读 6 已读  ',
   step_status          int(5) comment '(冗余字段) 1 发阅中 2 待办传阅 3 已完成',
   open_time            datetime comment '记录打开传阅的时间',
   if_confirm           bit(1) default 0 comment 'false 未确认 true 已确认 (传阅开封, 不一定是传阅确认   但传阅确认必须是传阅开封
            )',
   affirm_time          datetime comment '确认时间',
   remark               varchar(100) comment '当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注',
   confirm_record       varchar(150) comment '该字段用于记录收件人在确认传阅时的  确认信息 + 确认时间 ',
   serial_num           int default 0 comment '序号',
   afresh_confim        bit(1) default 0 comment '该字段用于重新确认传阅, false 未重新确认 true 重新确认 ',
   ac_record            longtext comment '该字段用于记录收件人在重新 确认传阅时的  确认信息 + 确认时间 ',
   afresh_remark        varchar(100) comment '当收到传阅后, 需要对收到的传阅进行确认, 同时要给出确认信息备注(重新)',
   mh_time              datetime comment '确认时间(重新)',
   receive_attention    bit(1) default 0 comment '收件人的关注状态:  0 为未关注 （false），1 已关注（true）',
   re_differentiate     bigint default 0 comment '区别是谁添加的联系人, 存放添加该联系人的用户ID',
   primary key (receive_id)
);

/*==============================================================*/
/* Table: role_menu_tbl                                         */
/*==============================================================*/
create table role_menu_tbl
(
   role_id              bigint not null,
   menu_id              bigint not null,
   primary key (role_id, menu_id)
);

/*==============================================================*/
/* Table: role_tbl                                              */
/*==============================================================*/
create table role_tbl
(
   role_id              bigint not null auto_increment,
   parent_id            bigint,
   role_name            varchar(32) not null,
   role_description     varchar(256),
   status               bit comment '0 为失效 （false），1 为正常（true）',
   primary key (role_id)
);

/*==============================================================*/
/* Table: system_backup_log_tbl                                 */
/*==============================================================*/
create table system_backup_log_tbl
(
   log_id               bigint not null auto_increment,
   operator             varchar(64) comment '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
   log_time             datetime not null,
   action               longtext not null,
   ip                   varchar(32) not null,
   identity_info        varchar(64) comment '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
            不和用户表直接外键关联。',
   primary key (log_id)
);

/*==============================================================*/
/* Table: system_log_tbl                                        */
/*==============================================================*/
create table system_log_tbl
(
   log_id               bigint not null auto_increment,
   operator             varchar(64) comment '可阅读的操作人信息，一般当前用户的昵称。也可以是用户真是姓名，试系统情况确定。',
   log_time             datetime not null,
   action               longtext not null,
   ip                   varchar(32) not null,
   identity_info        varchar(64) comment '可以是用户名，用户实体id，session id等描述用户的信息，以便核对。
            不和用户表直接外键关联。',
   primary key (log_id)
);

/*==============================================================*/
/* Table: tag_tbl                                               */
/*==============================================================*/
create table tag_tbl
(
   tag_id               bigint not null auto_increment comment '标签ID',
   tag_type             varchar(50) comment '上传文件的类型',
   storage_type         varchar(50) comment '存放文件的方式, 网盘还是本地',
   self_name            varchar(50) comment '自定义的标识',
   company_name         varchar(50) comment '公司名称',
   label_status         int(5) default 0 comment '0 正常  1 废弃不用',
   primary key (tag_id)
);

/*==============================================================*/
/* Table: update_record_tbl                                     */
/*==============================================================*/
create table update_record_tbl
(
   update_id            bigint not null auto_increment comment '修改ID',
   item_id              bigint comment '附件文件ID',
   receive_id           bigint comment '收件id',
   update_time          datetime comment '修改的时间',
   update_action        int(5) comment '0 为新增 ; 1 为修改 ; 2 为删除',
   primary key (update_id)
);

/*==============================================================*/
/* Table: user_collection_tbl                                   */
/*==============================================================*/
create table user_collection_tbl
(
   collection_id        bigint not null auto_increment comment '收藏id',
   mail_id              bigint comment '传阅id',
   user_id              bigint not null comment '收藏人id',
   work_code            varchar(50) comment '收藏人工作编号',
   last_name            varchar(50) comment '收藏人的姓名',
   collection_time      datetime comment '收藏时间',
   differentiate        varchar(50) comment '区别是: 已发传阅人收藏的记录或是已收传阅人的收藏记录',
   primary key (collection_id)
);

/*==============================================================*/
/* Table: user_info_tbl                                         */
/*==============================================================*/
create table user_info_tbl
(
   user_id              bigint not null,
   last_name            varchar(32),
   first_name           varchar(32),
   user_name            varchar(64),
   email                varchar(32),
   phone_number         varchar(32),
   remark               varchar(64),
   primary key (user_id)
);

/*==============================================================*/
/* Table: user_mssage_tbl                                       */
/*==============================================================*/
create table user_mssage_tbl
(
   user_mssage_id       bigint not null auto_increment comment '用户信息ID',
   user_id              integer comment '用户ID',
   work_code            varchar(50) comment '用户工作编号',
   last_name            varchar(50) comment '用户的姓名',
   login_id             varchar(50) comment '系统账号(登录名)',
   dept_fullname        varchar(50) comment '部门全称',
   full_name            varchar(50) comment '分部全称',
   department_id        varchar(50) comment '用户部门ID',
   subcompany_id1       varchar(50) comment '用户分部ID',
   status               varchar(50) comment '用户状态',
   dsporder             float comment '显示顺序',
   box_session          varchar(64) comment '网盘session',
   create_time          datetime comment '创建时间',
   update_time          datetime comment '更新时间',
   primary key (user_mssage_id)
);

/*==============================================================*/
/* Table: user_role_tbl                                         */
/*==============================================================*/
create table user_role_tbl
(
   user_id              bigint not null,
   role_id              bigint not null,
   primary key (user_id, role_id)
);

/*==============================================================*/
/* Table: user_tbl                                              */
/*==============================================================*/
create table user_tbl
(
   user_id              bigint not null auto_increment,
   account_name         varchar(32) not null,
   nick_name            varchar(64),
   create_time          datetime not null,
   user_password        varchar(128) not null,
   admin                bit(1) not null default 0,
   enabled              bit(1) not null comment '0 为失效 （false），1 为正常（true）',
   last_login           datetime,
   priority             int(4) not null,
   soft_delete          bit(1) not null default 0,
   access_token         varchar(128),
   access_token_expire  datetime,
   primary key (user_id)
);

alter table attachment_item_tbl add constraint FK_Reference_12 foreign key (mail_id)
      references mail_tbl (mail_id) on delete restrict on update restrict;

alter table discuss_tbl add constraint FK_Reference_10 foreign key (mail_id)
      references mail_tbl (mail_id) on delete restrict on update restrict;

alter table menu_tbl add constraint FK_Reference_7 foreign key (parent_menu_id)
      references menu_tbl (menu_id) on delete restrict on update restrict;

alter table receive_tbl add constraint FK_Reference_11 foreign key (mail_id)
      references mail_tbl (mail_id) on delete restrict on update restrict;

alter table role_menu_tbl add constraint FK_Reference_8 foreign key (role_id)
      references role_tbl (role_id) on delete restrict on update restrict;

alter table role_menu_tbl add constraint FK_Reference_9 foreign key (menu_id)
      references menu_tbl (menu_id) on delete restrict on update restrict;

alter table role_tbl add constraint FK_Reference_6 foreign key (parent_id)
      references role_tbl (role_id) on delete restrict on update restrict;

alter table update_record_tbl add constraint FK_Reference_13 foreign key (receive_id)
      references receive_tbl (receive_id) on delete restrict on update restrict;

alter table update_record_tbl add constraint FK_Reference_15 foreign key (item_id)
      references attachment_item_tbl (item_id) on delete restrict on update restrict;

alter table user_collection_tbl add constraint FK_Reference_14 foreign key (mail_id)
      references mail_tbl (mail_id) on delete restrict on update restrict;

alter table user_info_tbl add constraint FK_Reference_3 foreign key (user_id)
      references user_tbl (user_id) on delete restrict on update restrict;

alter table user_role_tbl add constraint FK_Reference_1 foreign key (user_id)
      references user_tbl (user_id) on delete restrict on update restrict;

alter table user_role_tbl add constraint FK_Reference_2 foreign key (role_id)
      references role_tbl (role_id) on delete restrict on update restrict;

