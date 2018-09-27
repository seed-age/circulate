package com.sunnsoft.sloa.actions.system;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

public class SaveSetting extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	private String password;
	private String oldPassword;
	private PasswordEncoder passwordEncoder;
	private UserService userService;
	private String json;
	
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getJson() {
		return json;
	}

	public String getPassword() {
		return password;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String execute() throws Exception {
		SystemUser sysuser = UserUtils.getCurrentUser();
		User user = userService.findById(sysuser.getUserId());
		if(StringUtils.isNotEmpty(password)){
			if(password.length() >= 6  && password.length() <= 20){
				if(user != null){
					if(!this.passwordEncoder.matches(oldPassword, user.getUserPassword())){
						this.msg="请输入正确的旧密码";
						return "failure";
					}
					user.setUserPassword(this.passwordEncoder.encode(password));
					userService.update(user);
				}
			}else{
				this.msg = "密码长度必须6~20个字符";
				return Results.GLOBAL_FAILURE_JSON;
			}
		}
		return Results.GLOBAL_SUCCESS_JSON;
	}
	
	

}
