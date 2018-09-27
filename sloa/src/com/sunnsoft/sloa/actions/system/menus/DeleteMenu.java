package com.sunnsoft.sloa.actions.system.menus;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

import java.util.Iterator;
import java.util.List;

public class DeleteMenu extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private MenuService menuService;
	private String msg;
	private long loadTreeId;//特殊，用来让ext刷新树形分支
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getLoadTreeId() {
		return loadTreeId;
	}
	public void setLoadTreeId(long loadTreeId) {
		this.loadTreeId = loadTreeId;
	}
	@Override
	public String execute() throws Exception {
		if(UserUtils.getCurrentUser().isAdmin()){
			Menu menu = this.menuService.findById(id);
			if(menu != null){
				Menu parent = menu.getMenu();
				MenuHelper helper = this.menuService.createHelper();
				if(parent == null){
					helper.getMenu().IsNull();
				}else{
					this.loadTreeId = parent.getMenuId();
					helper.getMenu().Eq(parent);
				}
				if(menu.getRoles().size() > 0){
					this.msg = "此菜单仍有使用角色，不能被删除";
					return Results.GLOBAL_FAILURE_JSON;
				}
				if(menu.getMenus().size() > 0){
					this.msg = "此菜单有子菜单，请先删除子菜单";
					return Results.GLOBAL_FAILURE_JSON;
				}
				
				this.menuService.delete(menu);
				List<Menu> menus = helper.getIndexValue().Asc().list();
				int index = 0;
				for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
					Menu orderMenu = iterator.next();
					orderMenu.setIndexValue(index++);
					this.menuService.update(orderMenu);
				}
			}
		}else{
			this.msg = "不是管理员不能删除菜单";
			return Results.GLOBAL_FAILURE_JSON;
		}
		return Results.SUCCESS;
	}
	
	
}
