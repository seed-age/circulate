package com.sunnsoft.sloa.actions.system;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.struts2.Results;

public class LoadSettingData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	private String json;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	//TODO 暂时只有修改密码功能，所以无数据加载。
	@Override
	public String execute() throws Exception {
		this.json = "{}";
		return Results.GLOBAL_FORM_JSON;
	}
	
	
	
}
