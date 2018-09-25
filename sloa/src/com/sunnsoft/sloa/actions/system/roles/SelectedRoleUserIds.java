package com.sunnsoft.sloa.actions.system.roles;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.util.struts2.Results;

import org.gteam.util.FastJSONUtils;

public class SelectedRoleUserIds extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long roleId;
	
	private String json;
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	@Override
	public String execute() throws Exception {
		UserHelper helper = Services.getUserService().createHelper();
		List<Long> selectedUserIds = helper.enterRoles().getRoleId().Eq(roleId).back2Users().getUserId().listProperty(true);
 		json = FastJSONUtils.getJsonHelper().toJSONString(selectedUserIds, true);
		return Results.GLOBAL_JSON;
	}
	

}
