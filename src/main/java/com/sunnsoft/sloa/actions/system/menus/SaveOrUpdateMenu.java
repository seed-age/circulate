package com.sunnsoft.sloa.actions.system.menus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

@SuppressWarnings("unchecked")
public class SaveOrUpdateMenu extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long parentId;
	private Long menuId;
	private String menuText;
	private String extId;
	private String icon;
	private String iconCls;
	private String actionPath;
	private boolean enabled;
	private boolean leaf;
	private boolean expanded;
	private String layoutAttr;
	private MenuService menuService;
	private RoleService roleService;
	private Long loadTreeId;//特殊，用来让ext刷新树形分支
	private String msg;
	
	private List<Long> roleIds = new ArrayList<Long>();
	
	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getLayoutAttr() {
		return layoutAttr;
	}

	public void setLayoutAttr(String layoutAttr) {
		this.layoutAttr = layoutAttr;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public Long getLoadTreeId() {
		return loadTreeId;
	}

	public void setLoadTreeId(Long loadTreeId) {
		this.loadTreeId = loadTreeId;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "当前用户不是管理员，添加或修改菜单";
			return Results.GLOBAL_FAILURE_JSON;
		}
		Menu menu = null;
		boolean isNew = false;
		if(menuId == null || menuId <= 0){
			menu = new Menu();
			isNew = true;
		}else{
			menu = menuService.findById(menuId);
			if(menu == null){
				this.msg="找不到菜单对象:"+menuId;
				return Results.GLOBAL_FAILURE_JSON;
			}
		}
		
		Menu parentMenu = menu.getMenu();
		
		if(parentMenu == null && parentId != null){
			parentMenu = menuService.findById(parentId);
			menu.setMenu(parentMenu);
		}
		
		//获取最大的index.并在此基础上加1
		MenuHelper helper  = this.menuService.createHelper();
		if(parentMenu == null){
			helper.getMenu().IsNull();
		}else{
			helper.getMenu().Eq(parentMenu);
		}
		if(isNew){
			helper.getIndexValue().Desc().limit(0, 1);
			Menu maxIndexMenu = helper.uniqueResult();
			if(maxIndexMenu != null){
				menu.setIndexValue(maxIndexMenu.getIndexValue()+1);
			}
		}
		
		menu.setActionPath(actionPath);
		menu.setEnabled(enabled);
		menu.setExpanded(expanded);
		menu.setExtId(extId);
		menu.setMenuText(menuText);
		if(StringUtils.isNotEmpty(icon)){
			menu.setIcon(icon);
		}else{
			menu.setIcon(null);
		}
		if(StringUtils.isNotEmpty(iconCls)){
			menu.setIconCls(iconCls);
		}else{
			menu.setIconCls(null);
		}
		menu.setLayout(layoutAttr);
		menu.setLeaf(leaf);
		menuService.saveOrUpdate(menu);
		if(parentMenu != null){
			loadTreeId = parentMenu.getMenuId();
		}else{
			loadTreeId = 0l;//树的根节点
		}
		// save roles
		saveRoles(menu);
		
		return Results.SUCCESS;
	}

	private void saveRoles(Menu menu) {
		List roles = this.roleService.getAll();
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			List<Menu> roleMenus = role.getMenus();
			if(this.roleIds.contains(role.getRoleId())){
				if(!roleMenus.contains(menu)){
					roleMenus.add(menu);
					this.roleService.update(role);
				}
			}else if(roleMenus.contains(menu)){
				roleMenus.remove(menu);
				this.roleService.update(role);
			}
		}
	}
	
}
