package com.sunnsoft.sloa.actions.system;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.config.Config;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Login extends ActionSupport implements ServletResponseAware,ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private Config config;
	
	public void setConfig(Config config) {
		this.config = config;
	}

	public String getPublicKey(){
		return this.config.getLoginPublicKey();
	}
	
	private String error;
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	private boolean logout;

	public boolean isLogout() {
		return logout;
	}

	public void setLogout(boolean logout) {
		this.logout = logout;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;	
	}

	@Override
	public String execute() throws Exception {
		this.response.addHeader("ext-login", "true");//重要，如果登录系统后，用户session失效后点击按钮会跳到Login页面，EXT的ajax功能通过此request head 初始化内部登录框。
		if(this.logout){
			this.response.addHeader("ext-logout", "true");//重要,在csrf模式下只能post提交请求，因此该标记表示当前动作是登录，此时应该reload浏览器地址自动跳转到登陆页面，此问题只有使用extjs框架才会有。
		}
		Object csrf =request.getAttribute("_csrf");
		if( csrf != null && csrf instanceof CsrfToken){
			this.response.addHeader("X-CSRF-TOKEN", ((CsrfToken)csrf).getToken());
		}
		if(this.error != null){
			this.response.addHeader("ext-error", this.error);//根据不同错误弹出不同的提示。
		}
		return "success";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	
}
