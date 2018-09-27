package com.sunnsoft.util.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 系统管理员列表，以登录账号（KEY）和加密后的密码组成（VALUE）; 注意，此处的密码不作为CAS中央认证系统的验证，所以此处的密码对于CAS是无效的。
 * 
 * @author llade
 * 
 */
public class AdminList {

	private Map<String, String> adminMap = new HashMap<String, String>();

	public void setAdminMap(Map<String, String> adminMap) {
		this.adminMap = adminMap;
	}

	public Set<String> getAdminNameList() {
		Set<String> result = new HashSet<String>();
		result.addAll(adminMap.keySet());
		return result;
	}

	public String getEncryptPassword(String userName) {
		return adminMap.get(userName);
	}

	public boolean isInAdminList(String userName) {
		return adminMap.containsKey(userName);
	}
}
