package com.sunnsoft.ext.base;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.struts2.Results;

public class Jsonp extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String callback;
	protected String json;

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Override
	public String execute() throws Exception {
		return Results.GLOBAL_JSONP;
	}
	
	

}
