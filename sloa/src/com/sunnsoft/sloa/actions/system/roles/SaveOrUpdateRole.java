package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class SaveOrUpdateRole extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long roleId;
	private String roleName;
	private String roleDescription;
	private boolean status;
	private RoleService roleService;
	private UserService userService;
	private MenuService menuService;
	private Long loadTreeId;//特殊，用来让ext刷新树形分支
	private String msg;
	private List<Long> userIds = new ArrayList<Long>(0);
	private List<Long> menuIds = new ArrayList<Long>(0);

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "当前用户不是管理员，添加或修改角色";
			return Results.GLOBAL_FAILURE_JSON;
		}
		Role role = null;
		if(roleId == null || roleId <= 0){
			role = new Role();
			roleId = 0l;
		}else{
			role = roleService.findById(roleId);
			if(role == null){
				this.msg="找不到角色:"+roleId;
				return Results.GLOBAL_FAILURE_JSON;
			}
		}
		
		role.setRoleName(roleName);
		role.setRoleDescription(roleDescription);
		role.setStatus(status);
		
		//设置角色关联菜单
		List<Menu> menusNew = this.menuService.findByIds(this.menuIds.toArray(new Long[0]));
		List<Menu> menus = role.getMenus();
		for (Iterator iterator = menus.iterator(); iterator.hasNext();) {
			Menu menu = (Menu) iterator.next();
			if(!menusNew.remove(menu)){//表示不需要关联这个menu了
				iterator.remove();
			}
		}
		menus.addAll(menusNew);
		
		roleService.saveOrUpdate(role);
		
		loadTreeId = 0l;//树的根节点
		
		//设置角色关联用户
		List<User> users = role.getUsers();
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if(!userIds.remove(user.getUserId())){//表示不需要关联这个user了
				user.getRoles().remove(role);
				this.userService.update(user);//必须在user端更新多对多关联，因为user端inverse = false;
			}
		}
		//剩下的userIds，加入新的关系中。
		List usersNew = this.userService.findByIds(this.userIds.toArray(new Long[0]));
		for (Iterator iterator = usersNew.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			user.getRoles().add(role);
			this.userService.update(user);//必须在user端更新多对多关联，因为user端inverse = false;
		}
		
		
		
		return Results.SUCCESS;
	}

}
