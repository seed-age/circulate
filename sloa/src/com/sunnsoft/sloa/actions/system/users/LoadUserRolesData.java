package com.sunnsoft.sloa.actions.system.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Role;
import com.sunnsoft.sloa.helper.RoleHelper;
import com.sunnsoft.sloa.service.RoleService;
import com.sunnsoft.util.struts2.Results;

import org.gteam.util.FastJSONUtils;

@SuppressWarnings("unchecked")
public class LoadUserRolesData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private RoleService roleService;
	
	private String json;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public RoleService getRoleService() {
		return roleService;
	}
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	@Override
	public String execute() throws Exception {
		RoleHelper helper = new RoleHelper(roleService);
		List<Role> roles = helper.getStatus().Eq(true).enterUsers().getUserId().Eq(userId).back2Roles().list();
		List<Role> allRoles = helper.newOne().getStatus().Eq(true).list();
		List<Map> beanList = new ArrayList<Map>();
        for (Iterator<Role> iterator = allRoles.iterator(); iterator.hasNext();) {
			Role role = iterator.next();
			Map<String, Object> roleMap = new HashMap<String, Object>();
			roleMap.put("text", role.getRoleName());
			roleMap.put("leaf", true);
			if(roles.contains(role)){
				roleMap.put("checked", true);
			}else{
				roleMap.put("checked", false);
			}
			roleMap.put("id", role.getRoleId());
			beanList.add(roleMap);
		}
 		json = FastJSONUtils.getJsonHelper().toJSONString(beanList, true);
		return Results.GLOBAL_JSON;
	}
	

}
