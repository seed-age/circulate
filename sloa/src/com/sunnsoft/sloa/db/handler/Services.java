package com.sunnsoft.sloa.db.handler;

public class Services {

	private static AttachmentItemService attachmentItemService;
	
	public static AttachmentItemService getAttachmentItemService(){
		return attachmentItemService;
	}
	
	static void setAttachmentItemService(AttachmentItemService service){
		attachmentItemService = service;
	}
	
	public static AttachmentItemHelper attachmentItemHelper(){
		return attachmentItemService.createHelper();
	}
	private static DictionaryService dictionaryService;
	
	public static DictionaryService getDictionaryService(){
		return dictionaryService;
	}
	
	static void setDictionaryService(DictionaryService service){
		dictionaryService = service;
	}
	
	public static DictionaryHelper dictionaryHelper(){
		return dictionaryService.createHelper();
	}
	private static DiscussService discussService;
	
	public static DiscussService getDiscussService(){
		return discussService;
	}
	
	static void setDiscussService(DiscussService service){
		discussService = service;
	}
	
	public static DiscussHelper discussHelper(){
		return discussService.createHelper();
	}
	private static HrmdepartmentService hrmdepartmentService;
	
	public static HrmdepartmentService getHrmdepartmentService(){
		return hrmdepartmentService;
	}
	
	static void setHrmdepartmentService(HrmdepartmentService service){
		hrmdepartmentService = service;
	}
	
	public static HrmdepartmentHelper hrmdepartmentHelper(){
		return hrmdepartmentService.createHelper();
	}
	private static HrmsubcompanyService hrmsubcompanyService;
	
	public static HrmsubcompanyService getHrmsubcompanyService(){
		return hrmsubcompanyService;
	}
	
	static void setHrmsubcompanyService(HrmsubcompanyService service){
		hrmsubcompanyService = service;
	}
	
	public static HrmsubcompanyHelper hrmsubcompanyHelper(){
		return hrmsubcompanyService.createHelper();
	}
	private static MailService mailService;
	
	public static MailService getMailService(){
		return mailService;
	}
	
	static void setMailService(MailService service){
		mailService = service;
	}
	
	public static MailHelper mailHelper(){
		return mailService.createHelper();
	}
	private static MenuService menuService;
	
	public static MenuService getMenuService(){
		return menuService;
	}
	
	static void setMenuService(MenuService service){
		menuService = service;
	}
	
	public static MenuHelper menuHelper(){
		return menuService.createHelper();
	}
	private static PersistentLoginsService persistentLoginsService;
	
	public static PersistentLoginsService getPersistentLoginsService(){
		return persistentLoginsService;
	}
	
	static void setPersistentLoginsService(PersistentLoginsService service){
		persistentLoginsService = service;
	}
	
	public static PersistentLoginsHelper persistentLoginsHelper(){
		return persistentLoginsService.createHelper();
	}
	private static ReceiveService receiveService;
	
	public static ReceiveService getReceiveService(){
		return receiveService;
	}
	
	static void setReceiveService(ReceiveService service){
		receiveService = service;
	}
	
	public static ReceiveHelper receiveHelper(){
		return receiveService.createHelper();
	}
	private static RoleService roleService;
	
	public static RoleService getRoleService(){
		return roleService;
	}
	
	static void setRoleService(RoleService service){
		roleService = service;
	}
	
	public static RoleHelper roleHelper(){
		return roleService.createHelper();
	}
	private static SystemBackupLogService systemBackupLogService;
	
	public static SystemBackupLogService getSystemBackupLogService(){
		return systemBackupLogService;
	}
	
	static void setSystemBackupLogService(SystemBackupLogService service){
		systemBackupLogService = service;
	}
	
	public static SystemBackupLogHelper systemBackupLogHelper(){
		return systemBackupLogService.createHelper();
	}
	private static SystemLogService systemLogService;
	
	public static SystemLogService getSystemLogService(){
		return systemLogService;
	}
	
	static void setSystemLogService(SystemLogService service){
		systemLogService = service;
	}
	
	public static SystemLogHelper systemLogHelper(){
		return systemLogService.createHelper();
	}
	private static TagService tagService;
	
	public static TagService getTagService(){
		return tagService;
	}
	
	static void setTagService(TagService service){
		tagService = service;
	}
	
	public static TagHelper tagHelper(){
		return tagService.createHelper();
	}
	private static UpdateRecordService updateRecordService;
	
	public static UpdateRecordService getUpdateRecordService(){
		return updateRecordService;
	}
	
	static void setUpdateRecordService(UpdateRecordService service){
		updateRecordService = service;
	}
	
	public static UpdateRecordHelper updateRecordHelper(){
		return updateRecordService.createHelper();
	}
	private static UserService userService;
	
	public static UserService getUserService(){
		return userService;
	}
	
	static void setUserService(UserService service){
		userService = service;
	}
	
	public static UserHelper userHelper(){
		return userService.createHelper();
	}
	private static UserCollectionService userCollectionService;
	
	public static UserCollectionService getUserCollectionService(){
		return userCollectionService;
	}
	
	static void setUserCollectionService(UserCollectionService service){
		userCollectionService = service;
	}
	
	public static UserCollectionHelper userCollectionHelper(){
		return userCollectionService.createHelper();
	}
	private static UserInfoService userInfoService;
	
	public static UserInfoService getUserInfoService(){
		return userInfoService;
	}
	
	static void setUserInfoService(UserInfoService service){
		userInfoService = service;
	}
	
	public static UserInfoHelper userInfoHelper(){
		return userInfoService.createHelper();
	}
	private static UserMssageService userMssageService;
	
	public static UserMssageService getUserMssageService(){
		return userMssageService;
	}
	
	static void setUserMssageService(UserMssageService service){
		userMssageService = service;
	}
	
	public static UserMssageHelper userMssageHelper(){
		return userMssageService.createHelper();
	}
}
