package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.helper.RoleHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

public class DeleteRole extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private RoleService roleService;
	private String msg;
	private long loadTreeId;//特殊，用来让ext刷新树形分支
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
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
			Role role = this.roleService.findById(id);
			if(role != null){
				Role parent = role.getRole();
				RoleHelper helper = this.roleService.createHelper();
				if(parent == null){
					helper.getRole().IsNull();
				}else{
					this.loadTreeId = parent.getRoleId();
					helper.getRole().Eq(parent);
				}
				if(role.getRoles().size() > 0){
					this.msg = "此角色有子角色，请先删除子角色";
					return Results.GLOBAL_FAILURE_JSON;
				}
				if(role.getUsers().size() > 0){
					this.msg = "请先清空此角色的用户";
					return Results.GLOBAL_FAILURE_JSON;
				}
				if(role.getMenus().size() > 0){
					this.msg = "请先清空此角色的菜单";
					return Results.GLOBAL_FAILURE_JSON;
				}
				
				this.roleService.delete(role);
			}
		}else{
			this.msg = "不是管理员不能删除角色";
			return Results.GLOBAL_FAILURE_JSON;
		}
		return Results.SUCCESS;
	}
	
	
}
