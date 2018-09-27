package com.sunnsoft.sloa.actions.system.menus;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.util.struts2.Results;

public class LoadMenusNodeData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuService menuService;
	
	private Long node;
	private boolean self;
	
	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

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
		MenuHelper helper = this.menuService.createHelper();
		if(self){//是否单个Menu本身，还是所选节点的所有子节点。
			helper.getMenuId().Eq(node);
			json = helper
			.json().includeMenuText().includeIcon().includeIconCls().includeMenuId().includeExpanded().includeLeaf().includeActionPath()
			.replaceKeyForMenuText("text").replaceKeyForMenuId("id").replaceKeyForActionPath("action").uniqueJson()
			;
		}else{
			if(node==null || node == 0l){
				helper.getMenu().IsNull();
			}else{
				helper.enterMenu().getMenuId().Eq(node);
			}
			json = helper.getIndexValue().Asc()
			.json().includeMenuText().includeIcon().includeIconCls().includeMenuId().includeExpanded().includeLeaf().includeActionPath()
			.replaceKeyForMenuText("text").replaceKeyForMenuId("id").replaceKeyForActionPath("action").listJson();
			;
		}
		
		
		return Results.GLOBAL_JSON;
	}
	
}
