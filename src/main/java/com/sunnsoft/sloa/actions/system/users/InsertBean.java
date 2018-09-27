package com.sunnsoft.sloa.actions.system.users;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

@SuppressWarnings("unchecked")
public class InsertBean extends ActionSupport { 
	
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
	private String accountName;
	private String userPassword;
	private boolean admin;
	private String nickName;
	private boolean enabled;
	private String msg;
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
			this.msg = "当前用户不是管理员，不能添加账户";
			return Results.GLOBAL_FAILURE_JSON;
		}
		UserHelper helper = this.userService.createHelper();
		if(helper.getAccountName().Eq(accountName).rowCount() > 0){
			this.msg = "账号重复，请重新注册";
			return Results.GLOBAL_FAILURE_JSON;
		}
		User user = new User();
		user.setAccountName(accountName);
		user.setAdmin(admin);
		user.setNickName(nickName);
		user.setUserPassword(this.passwordEncoder.encode(userPassword));
		user.setCreateTime(new Date());
		user.setEnabled(enabled);
		user.setPriority(0);
		setRoles(user,roleService,roleIds);
		this.userService.add(user);
		
		return Results.GLOBAL_SUCCESS_JSON;
	}
	/**
	 * 保存用户角色关系。
	 * 必须在User端保存关联关系，因为User端是inverse=false;
	 * @param user
	 */
	public static void setRoles(User user,RoleService roleService,List<Long> roleIds) {
		List roles = roleService.getAll();
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			List<Role> userRoles =  user.getRoles();
			if(roleIds != null && roleIds.contains(role.getRoleId())){
				if(!userRoles.contains(role)){
					userRoles.add(role);
				}
			}else if(userRoles.contains(role)){
				userRoles.remove(role);
			}
		}
	}

	

}
