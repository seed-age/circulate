package com.sunnsoft.security.csrf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

public class NotNeedCsrfProtectedUrlsRequestMatcher implements RequestMatcher {
	
	private static final Log logger = LogFactory.getLog(NotNeedCsrfProtectedUrlsRequestMatcher.class);
	
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private List<String> notNeedCsrfProtectedUrls = new ArrayList<String>(1);
	
	public void setNotNeedCsrfProtectedUrls(List<String> noNeedCsrfProtectedUrls) {
		this.notNeedCsrfProtectedUrls = noNeedCsrfProtectedUrls;
	}

	private final HashSet<String> allowedMethods = new HashSet<String>(
			Arrays.asList(new String[] { "GET", "HEAD", "TRACE", "OPTIONS" }));

	public boolean matches(HttpServletRequest request) {
		
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String uriWithoutContextPath = uri.substring(contextPath.length());
		
		if(logger.isDebugEnabled()){
			logger.debug("uriWithoutContextPath : " +uriWithoutContextPath);
		}
		
		for (Iterator<String> iterator = notNeedCsrfProtectedUrls.iterator(); iterator.hasNext();) {
			String url = iterator.next();
			if(antPathMatcher.match(url, uriWithoutContextPath)){
				if(logger.isDebugEnabled()){
					logger.debug("logout url detected,csrf protection skipped ");
				}
				return false;//不需要csrf检查。
			}
		}
			
		String requestMethod = request.getMethod();
		return (!(this.allowedMethods.contains(requestMethod)));//其他的都进行常规csrf检查
	}
	
}
