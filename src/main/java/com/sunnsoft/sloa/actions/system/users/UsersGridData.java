package com.sunnsoft.sloa.actions.system.users;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.util.struts2.Results;

import org.gteam.constants.SystemConstants;

public class UsersGridData extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	private String json;
	private int start;
	private int limit;
	private String sort;
	private String dir = "DESC";
	private String search;
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
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
		if(limit == 0){
			limit = 20;
		}
		int currentPage = start / limit + 1;
		if(StringUtils.isNotEmpty(sort)){
			if(SystemConstants.SORT_ASC.equals(dir)|| SystemConstants.SORT_DESC.equals(dir)){
				helper.getDc().addOrder(SystemConstants.SORT_ASC.equals(dir) ? Order.asc(sort):Order.desc(sort));
			}
		}
		if(StringUtils.isNotEmpty(search)){
			helper.startOr().getAccountName().Like(search).getNickName().Like(search).stopOr();
		}
		json = helper.distinctEntity().json()
			.excludeUserPassword().listPageJson(currentPage, limit);
		return Results.GLOBAL_JSON;
	}
	
	
}
