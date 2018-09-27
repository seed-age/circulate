package com.sunnsoft.sloa.actions.system.users;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

public class UpdateBean extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;
	private List<Long> roleIds;
	
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	public List<Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	private Long userId;
	private String accountName;
	private String userPassword;
	private boolean admin;
	private String nickName;
	private boolean enabled;
	private String msg;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "当前用户不是管理员，不能修改账户";
			return Results.GLOBAL_FAILURE_JSON;
		}
		UserHelper helper = this.userService.createHelper();
		User user = helper.getUserId().Eq(userId).uniqueResult();
		if(user == null){
			this.msg = "账号不存在或者被删除";
			return Results.GLOBAL_FAILURE_JSON;
		}else if(!user.getAccountName().equals(accountName)){
			if(helper.newOne().getAccountName().Eq(accountName).rowCount()>0){
				this.msg = "账号名已存在，请重新填写";
				return Results.GLOBAL_FAILURE_JSON;
			}
			user.setAccountName(accountName);
		}
		if(StringUtils.isNotEmpty(userPassword)){
			user.setUserPassword(this.passwordEncoder.encode(userPassword));
		}
		user.setEnabled(enabled);
		user.setNickName(nickName);
		user.setAdmin(admin);
		InsertBean.setRoles(user,roleService,roleIds);
		userService.update(user);
		return Results.GLOBAL_SUCCESS_JSON;
	}

}
