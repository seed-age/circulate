/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */

package com.sunnsoft.sloa.db.handler;

import com.sunnsoft.sloa.db.dao.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gteam.db.dao.ICommonDAO;

import java.lang.reflect.Field;
/**
 * @author 林宇民(llade)
 * @version 1.0
 *
 */
public class DaoHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private ICommonDAO commonDAO;

	private AttachmentItemDAO attachmentItemDAO;

	private DictionaryDAO dictionaryDAO;

	private DiscussDAO discussDAO;

	private HrmdepartmentDAO hrmdepartmentDAO;

	private HrmsubcompanyDAO hrmsubcompanyDAO;

	private MailDAO mailDAO;

	private MenuDAO menuDAO;

	private PersistentLoginsDAO persistentLoginsDAO;

	private ReceiveDAO receiveDAO;

	private RoleDAO roleDAO;

	private SystemBackupLogDAO systemBackupLogDAO;

	private SystemLogDAO systemLogDAO;

	private TagDAO tagDAO;

	private UpdateRecordDAO updateRecordDAO;

	private UserCollectionDAO userCollectionDAO;

	private UserDAO userDAO;

	private UserInfoDAO userInfoDAO;

	private UserMssageDAO userMssageDAO;

	public void setCommonDAO(ICommonDAO commonDAO){
		this.commonDAO = commonDAO;
	}

	public void setAttachmentItemDAO(AttachmentItemDAO attachmentItemDAO){
		this.attachmentItemDAO = attachmentItemDAO;
	}

	public void setDictionaryDAO(DictionaryDAO dictionaryDAO){
		this.dictionaryDAO = dictionaryDAO;
	}

	public void setDiscussDAO(DiscussDAO discussDAO){
		this.discussDAO = discussDAO;
	}

	public void setHrmdepartmentDAO(HrmdepartmentDAO hrmdepartmentDAO){
		this.hrmdepartmentDAO = hrmdepartmentDAO;
	}

	public void setHrmsubcompanyDAO(HrmsubcompanyDAO hrmsubcompanyDAO){
		this.hrmsubcompanyDAO = hrmsubcompanyDAO;
	}

	public void setMailDAO(MailDAO mailDAO){
		this.mailDAO = mailDAO;
	}

	public void setMenuDAO(MenuDAO menuDAO){
		this.menuDAO = menuDAO;
	}

	public void setPersistentLoginsDAO(PersistentLoginsDAO persistentLoginsDAO){
		this.persistentLoginsDAO = persistentLoginsDAO;
	}

	public void setReceiveDAO(ReceiveDAO receiveDAO){
		this.receiveDAO = receiveDAO;
	}

	public void setRoleDAO(RoleDAO roleDAO){
		this.roleDAO = roleDAO;
	}

	public void setSystemBackupLogDAO(SystemBackupLogDAO systemBackupLogDAO){
		this.systemBackupLogDAO = systemBackupLogDAO;
	}

	public void setSystemLogDAO(SystemLogDAO systemLogDAO){
		this.systemLogDAO = systemLogDAO;
	}

	public void setTagDAO(TagDAO tagDAO){
		this.tagDAO = tagDAO;
	}

	public void setUpdateRecordDAO(UpdateRecordDAO updateRecordDAO){
		this.updateRecordDAO = updateRecordDAO;
	}

	public void setUserCollectionDAO(UserCollectionDAO userCollectionDAO){
		this.userCollectionDAO = userCollectionDAO;
	}

	public void setUserDAO(UserDAO userDAO){
		this.userDAO = userDAO;
	}

	public void setUserInfoDAO(UserInfoDAO userInfoDAO){
		this.userInfoDAO = userInfoDAO;
	}

	public void setUserMssageDAO(UserMssageDAO userMssageDAO){
		this.userMssageDAO = userMssageDAO;
	}

	public ICommonDAO getCommonDAO(){
		return this.commonDAO;
	}

	public AttachmentItemDAO getAttachmentItemDAO(){
		return this.attachmentItemDAO;
	}

	public DictionaryDAO getDictionaryDAO(){
		return this.dictionaryDAO;
	}

	public DiscussDAO getDiscussDAO(){
		return this.discussDAO;
	}

	public HrmdepartmentDAO getHrmdepartmentDAO(){
		return this.hrmdepartmentDAO;
	}

	public HrmsubcompanyDAO getHrmsubcompanyDAO(){
		return this.hrmsubcompanyDAO;
	}

	public MailDAO getMailDAO(){
		return this.mailDAO;
	}

	public MenuDAO getMenuDAO(){
		return this.menuDAO;
	}

	public PersistentLoginsDAO getPersistentLoginsDAO(){
		return this.persistentLoginsDAO;
	}

	public ReceiveDAO getReceiveDAO(){
		return this.receiveDAO;
	}

	public RoleDAO getRoleDAO(){
		return this.roleDAO;
	}

	public SystemBackupLogDAO getSystemBackupLogDAO(){
		return this.systemBackupLogDAO;
	}

	public SystemLogDAO getSystemLogDAO(){
		return this.systemLogDAO;
	}

	public TagDAO getTagDAO(){
		return this.tagDAO;
	}

	public UpdateRecordDAO getUpdateRecordDAO(){
		return this.updateRecordDAO;
	}

	public UserCollectionDAO getUserCollectionDAO(){
		return this.userCollectionDAO;
	}

	public UserDAO getUserDAO(){
		return this.userDAO;
	}

	public UserInfoDAO getUserInfoDAO(){
		return this.userInfoDAO;
	}

	public UserMssageDAO getUserMssageDAO(){
		return this.userMssageDAO;
	}

	public void init() throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = DaoHandler.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			logger.debug("fields field:"+fields[i]);
			if(fields[i].getName().endsWith("DAO")){
				if(fields[i].get(this) == null)throw new RuntimeException("没有注入DAO："+fields[i].getName());
			}
		}
	}

}
