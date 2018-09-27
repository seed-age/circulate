package com.sunnsoft.sloa.db.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Component
public class ServicesInitializer implements ApplicationContextAware{

	@PostConstruct
	public void init(){
		Services.setAttachmentItemService((AttachmentItemService)ctx.getBean("attachmentItemService"));
		Assert.notNull(Services.getAttachmentItemService());
		Services.setDictionaryService((DictionaryService)ctx.getBean("dictionaryService"));
		Assert.notNull(Services.getDictionaryService());
		Services.setDiscussService((DiscussService)ctx.getBean("discussService"));
		Assert.notNull(Services.getDiscussService());
		Services.setHrmdepartmentService((HrmdepartmentService)ctx.getBean("hrmdepartmentService"));
		Assert.notNull(Services.getHrmdepartmentService());
		Services.setHrmsubcompanyService((HrmsubcompanyService)ctx.getBean("hrmsubcompanyService"));
		Assert.notNull(Services.getHrmsubcompanyService());
		Services.setMailService((MailService)ctx.getBean("mailService"));
		Assert.notNull(Services.getMailService());
		Services.setMenuService((MenuService)ctx.getBean("menuService"));
		Assert.notNull(Services.getMenuService());
		Services.setPersistentLoginsService((PersistentLoginsService)ctx.getBean("persistentLoginsService"));
		Assert.notNull(Services.getPersistentLoginsService());
		Services.setReceiveService((ReceiveService)ctx.getBean("receiveService"));
		Assert.notNull(Services.getReceiveService());
		Services.setRoleService((RoleService)ctx.getBean("roleService"));
		Assert.notNull(Services.getRoleService());
		Services.setSystemBackupLogService((SystemBackupLogService)ctx.getBean("systemBackupLogService"));
		Assert.notNull(Services.getSystemBackupLogService());
		Services.setSystemLogService((SystemLogService)ctx.getBean("systemLogService"));
		Assert.notNull(Services.getSystemLogService());
		Services.setTagService((TagService)ctx.getBean("tagService"));
		Assert.notNull(Services.getTagService());
		Services.setUpdateRecordService((UpdateRecordService)ctx.getBean("updateRecordService"));
		Assert.notNull(Services.getUpdateRecordService());
		Services.setUserService((UserService)ctx.getBean("userService"));
		Assert.notNull(Services.getUserService());
		Services.setUserCollectionService((UserCollectionService)ctx.getBean("userCollectionService"));
		Assert.notNull(Services.getUserCollectionService());
		Services.setUserInfoService((UserInfoService)ctx.getBean("userInfoService"));
		Assert.notNull(Services.getUserInfoService());
		Services.setUserMssageService((UserMssageService)ctx.getBean("userMssageService"));
		Assert.notNull(Services.getUserMssageService());
		System.out.println("Services Class initialized");
	}

	ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;
	}
}
