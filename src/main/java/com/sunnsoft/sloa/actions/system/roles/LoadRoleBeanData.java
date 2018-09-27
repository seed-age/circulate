package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.RoleHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.util.struts2.Results;

public class LoadRoleBeanData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private RoleService roleService;
	private String msg;
	private String json;
	
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	@Override
	public String execute() throws Exception {
		RoleHelper helper = this.roleService.createHelper();
		json = helper.getRoleId().Eq(id).json().setBooleanToString().uniqueJson();
		
		return Results.GLOBAL_FORM_JSON;
	}
	

}
