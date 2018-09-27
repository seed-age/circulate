package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.helper.RoleHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.hibernate.Each;
import org.gteam.util.FastJSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class LoadRolesNodeData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RoleService roleService;
	
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

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public String execute() throws Exception {
		RoleHelper helper = this.roleService.createHelper();
		if(self){//是否单个角色本身，还是所选节点的所有子节点。
			helper.getRoleId().Eq(node);
			Map map = new HashMap();
			Role role = helper.uniqueResult();
			if(role == null){
				json = "{}";
			}else{
				map.put("text", role.getRoleName());
				map.put("id", role.getRoleId());
				map.put("expanded",true);
				if(role.getRoles().size() == 0){
					map.put("leaf", true);
				}else{
					map.put("leaf", false);
				}
				json = FastJSONUtils.getJsonHelper().toJSONString(map, true);
			}
		}else{
			if(node==null || node == 0l){
				helper.getRole().IsNull();
			}else{
				helper.enterRole().getRoleId().Eq(node);
			}
			final List list = new ArrayList();
			helper.each(new Each<Role>(){

				@Override
				public void each(Role bean, List<Role> beans) {
					Map map = new HashMap();
					map.put("text", bean.getRoleName());
					map.put("id", bean.getRoleId());
					map.put("expanded",true);
					if(bean.getRoles().size() == 0){
						map.put("leaf", true);
					}else{
						map.put("leaf", false);
					}
					list.add(map);
				}
				
			});
			json = FastJSONUtils.getJsonHelper().toJSONString(list);
		}
		
		
		return Results.GLOBAL_JSON;
	}
	
}
