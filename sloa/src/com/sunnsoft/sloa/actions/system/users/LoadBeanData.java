package com.sunnsoft.sloa.actions.system.users;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.util.struts2.Results;

public class LoadBeanData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private String json;
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute() throws Exception {
		UserHelper helper = this.userService.createHelper();
		this.json = helper.getUserId().Eq(id).json().setBooleanToString().excludeUserPassword().uniqueJson();
		return Results.GLOBAL_FORM_JSON;
	}
	
	
}
