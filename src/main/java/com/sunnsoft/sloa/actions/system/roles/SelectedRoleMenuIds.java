package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.util.struts2.Results;
import org.gteam.util.FastJSONUtils;

import java.util.List;

public class SelectedRoleMenuIds extends ActionSupport {
	
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
		MenuHelper helper = Services.getMenuService().createHelper();
		List<Long> selectedMenuIds = helper.enterRoles().getRoleId().Eq(roleId).back2Menus().getMenuId().listProperty(true);
 		json = FastJSONUtils.getJsonHelper().toJSONString(selectedMenuIds, true);
		return Results.GLOBAL_JSON;
	}
	

}
