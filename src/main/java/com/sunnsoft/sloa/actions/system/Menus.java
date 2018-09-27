package com.sunnsoft.sloa.actions.system;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

public class Menus extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuService menuService;
	
	private Long node;
	
	private String json;
	
	
	public Long getNode() {
		return node;
	}

	public void setNode(Long node) {
		this.node = node;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Override
	public String execute() throws Exception {
		MenuHelper helper = menuService.createHelper();
		SystemUser sysuser = UserUtils.getCurrentUser();
		if(node==null || node == 0l){
			helper.getMenu().IsNull();
		}else{
			helper.enterMenu().getMenuId().Eq(node);
		}
		json = helper.distinctEntity().getEnabled().Eq(true).enterRoles().enterUsers().getUserId().Eq(sysuser.getUserId())
			.back2Roles().back2Menus().getIndexValue().Asc()
			.json().includeMenuText().includeIcon().includeIconCls().includeMenuId().includeExpanded().includeExtId().includeLeaf().includeActionPath()
			.replaceKeyForMenuText("text").replaceKeyForMenuId("id").replaceKeyForActionPath("action").listJson();
			;
		return Results.GLOBAL_JSON;
	}
	
}
