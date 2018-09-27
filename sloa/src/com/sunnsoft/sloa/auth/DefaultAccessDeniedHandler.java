package com.sunnsoft.sloa.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 林宇民 Andy (llade)
 *
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
	
	private String loginPageUrl;
	private boolean addContextPath = true;
	
	/**
	 * @param addContextPath the addContextPath to set
	 */
	public void setAddContextPath(boolean addContextPath) {
		this.addContextPath = addContextPath;
	}

	/**
	 * @param loginPageUrl the loginPageUrl to set
	 */
	public void setLoginPageUrl(String loginPageUrl) {
		this.loginPageUrl = loginPageUrl;
	}
	
	@PostConstruct
	public void init() {
		Assert.notNull(loginPageUrl, "loginPageUrl 不能为空");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exeception)
			throws IOException, ServletException {
		String context =  request.getContextPath();
		response.sendRedirect( !addContextPath ? loginPageUrl : context + loginPageUrl );
	}

}
