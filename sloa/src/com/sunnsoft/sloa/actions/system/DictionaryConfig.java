package com.sunnsoft.sloa.actions.system;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.util.base.DataDictonary;
import com.sunnsoft.util.struts2.Results;

public class DictionaryConfig extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String keyName;
	private String keyValue;
	private String msg;

	private DataDictonary userDictonary;
	
	public DataDictonary getUserDictonary() {
		return userDictonary;
	}


	public void setUserDictonary(DataDictonary userDictonary) {
		this.userDictonary = userDictonary;
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	public String getKeyValue() {
		return keyValue;
	}


	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	@Override
	public String execute() throws Exception {
		if(StringUtils.isEmpty(keyName)){
			this.msg = "keyName 不能为空";
			return Results.GLOBAL_FAILURE_JSON;
		}
		this.userDictonary.put(keyName, keyValue);
		return Results.GLOBAL_SUCCESS_JSON;
	}
	
}
