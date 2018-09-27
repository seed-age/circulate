package com.sunnsoft.sloa.actions.system.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.util.struts2.Results;

import org.gteam.util.FastJSONUtils;

@SuppressWarnings("unchecked")
public class LoadRoleUsersData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long roleId;
	private UserService userService;
	
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
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	public String execute() throws Exception {
		UserHelper helper = this.userService.createHelper();
		List<User> selectedUsers = helper.enterRoles().getRoleId().Eq(roleId).back2Users().list();
		List<User> allUsers = helper.newOne().getNickName().Asc().list();
		List<Map> beanList = new ArrayList<Map>();
        for (Iterator<User> iterator = allUsers.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", (user.getNickName()==null?"":user.getNickName())+"("+user.getAccountName()+")");
			if(user.isEnabled()){
				map.put("iconCls", "icon-accept");
			}else{
				map.put("iconCls", "icon-cancel");
			}
			map.put("leaf", true);
			if(selectedUsers.contains(user)){
				map.put("checked", true);
			}else{
				map.put("checked", false);
			}
			map.put("id", user.getUserId());
			beanList.add(map);
		}
 		json = FastJSONUtils.getJsonHelper().toJSONString(beanList, true);
		return Results.GLOBAL_JSON;
	}
	

}
