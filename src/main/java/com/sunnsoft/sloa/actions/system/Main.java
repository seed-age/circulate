package com.sunnsoft.sloa.actions.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.web.csrf.CsrfToken;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

public class Main extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private Config config;

	private HttpServletResponse response;

	private HttpServletRequest request;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public String getUserName() {
		return userName;
	}
	
	public boolean isDebugMode(){
		return this.config.isExtDebug();
	}
	
	public String getPublicKey(){
		return this.config.getLoginPublicKey();
	}

	@Override
	public String execute() throws Exception {
		SystemUser sysuser = UserUtils.getCurrentUser();
		userName = sysuser.getUserVo().getAccountName();
		Object csrf =request.getAttribute("_csrf");
		if( csrf != null && csrf instanceof CsrfToken){
			this.response.addHeader("X-CSRF-TOKEN", ((CsrfToken)csrf).getToken());
		}
		return Results.SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	

}
