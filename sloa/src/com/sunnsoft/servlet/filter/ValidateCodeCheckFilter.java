package com.sunnsoft.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunnsoft.servlet.ValidateCodeServlet;

public class ValidateCodeCheckFilter implements Filter {

	public static final String VALIDATE_CODE_FIELD_NAME = "validateCode";

	private String formLoginUrl ;
	private String failRedirectUrl ;

	public ValidateCodeCheckFilter(String formLoginUrl, String failRedirectUrl) {
		super();
		this.formLoginUrl = formLoginUrl;
		this.failRedirectUrl = failRedirectUrl;
	}
	
	public ValidateCodeCheckFilter() {
		super();
	}

	public String getFormLoginUrl() {
		return formLoginUrl;
	}

	public void setFormLoginUrl(String formLoginUrl) {
		this.formLoginUrl = formLoginUrl;
	}

	public String getFailRedirectUrl() {
		return failRedirectUrl;
	}

	public void setFailRedirectUrl(String failRedirectUrl) {
		this.failRedirectUrl = failRedirectUrl;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String sessionValidateCode = (String) httpRequest.getSession()
				.getAttribute(ValidateCodeServlet.VALIDATE_CODE_SESSION_NAME);
		String validateCode = request.getParameter(VALIDATE_CODE_FIELD_NAME);
		if (httpRequest.getRequestURI().equals(
				httpRequest.getContextPath() + formLoginUrl)) {
			if (sessionValidateCode != null
					&& sessionValidateCode.equals(validateCode)) {
				chain.doFilter(request, response);
				return;
			} else {
				((HttpServletResponse) response).sendRedirect(httpRequest
						.getContextPath()
						+ failRedirectUrl);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String param1 = filterConfig.getInitParameter("formLoginUrl");
		String param2 = filterConfig.getInitParameter("failRedirectUrl");
		if (param1 != null) {
			formLoginUrl = param1;
		}
		if (param2 != null) {
			failRedirectUrl = param2;
		}
	}

}
