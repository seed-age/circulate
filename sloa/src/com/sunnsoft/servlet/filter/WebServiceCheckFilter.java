package com.sunnsoft.servlet.filter;

import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebServiceCheckFilter implements Filter {

	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private String webServiceUrl = "";
	private String[] webServiceUrls = new String[0];
	private String failRedirectUrl = "";
	private String[] allowIps = new String[0];
	private boolean allowAll = false;

	public String[] getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(String[] allowIps) {
		this.allowIps = allowIps;
	}

	public boolean isAllowAll() {
		return allowAll;
	}

	public void setAllowAll(boolean allowAll) {
		this.allowAll = allowAll;
	}

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

	public void setWebServiceUrl(String webServiceUrl) {
		this.webServiceUrl = webServiceUrl;
	}

	public void setWebServiceUrls(String[] webServiceUrls) {
		this.webServiceUrls = webServiceUrls;
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
		String uri = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();
		String uriWithoutContextPath = uri.substring(contextPath.length());
		if (checkUrls(uriWithoutContextPath)) {
			String ip = request.getRemoteAddr();
			System.out.println("IP:" + ip);
			if (allowAll) {
				chain.doFilter(request, response);
				return;
			} else {
				for (int i = 0; i < allowIps.length; i++) {
					if (ip.startsWith(allowIps[i])) {
						chain.doFilter(request, response);
						return;
					}
				}
			}
			((HttpServletResponse) response).sendRedirect(httpRequest
					.getContextPath()
					+ failRedirectUrl);
			return;
		}
		chain.doFilter(request, response);
	}

	private boolean checkUrls(String uriWithoutContextPath) {
		for (int i = 0; i < webServiceUrls.length; i++) {
			if (antPathMatcher.match(webServiceUrls[i], uriWithoutContextPath)) {
				return true;
			}
		}
		return antPathMatcher.match(webServiceUrl, uriWithoutContextPath);
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
