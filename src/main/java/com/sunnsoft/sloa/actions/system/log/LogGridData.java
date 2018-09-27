package com.sunnsoft.sloa.actions.system.log;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.helper.SystemLogHelper;
import com.sunnsoft.sloa.service.SystemLogService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

import org.gteam.constants.SystemConstants;

public class LogGridData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SystemLogService systemLogService;

	public SystemLogService getSystemLogService() {
		return systemLogService;
	}

	public void setSystemLogService(SystemLogService systemLogService) {
		this.systemLogService = systemLogService;
	}

	private String json;
	private int start;
	private int limit;
	private String sort;
	private String dir = "DESC";
	private String msg;
	private String userName;
	private String ip;
	private Date startTime;
	private Date endTime;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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


	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "不是管理员无权查看日志";
			return Results.GLOBAL_FAILURE_JSON;
		}
		SystemLogHelper helper = this.systemLogService.createHelper();
		if(limit == 0){
			limit = 25;
		}
		int currentPage = start / limit + 1;
		if(StringUtils.isNotEmpty(sort)){
			if(SystemConstants.SORT_ASC.equals(dir)|| SystemConstants.SORT_DESC.equals(dir)){
				helper.getDc().addOrder(SystemConstants.SORT_ASC.equals(dir) ? Order.asc(sort):Order.desc(sort));
			}
		}
		
		if(StringUtils.isNotEmpty(ip)){
			helper.getIp().Like(ip);
		}
		
		if(StringUtils.isNotEmpty(userName)){
			helper.getOperator().Like(userName);
		}
		
		if(startTime != null){
			helper.getLogTime().Ge(startTime);
		}
		if(endTime != null){
			helper.getLogTime().Le(endTime);
		}
		
		json = helper.distinctEntity().json().listPageJson(currentPage, limit);
		return Results.GLOBAL_JSON;
	}
	
	
}
