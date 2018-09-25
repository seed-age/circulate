package com.sunnsoft.sloa.actions.system.menus;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.util.struts2.Results;

public class LoadMenuBeanData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private MenuService menuService;
	private String msg;
	private String json;
	
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
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
		MenuHelper helper = this.menuService.createHelper();
		json = helper.getMenuId().Eq(id).json().replaceKeyForLayout("layoutAttr").setBooleanToString().uniqueJson();
		return Results.GLOBAL_FORM_JSON;
	}
	

}
