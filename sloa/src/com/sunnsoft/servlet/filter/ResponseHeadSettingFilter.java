package com.sunnsoft.servlet.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author llade
 * @version 1.0 Creation date: Dec 17, 2008 12:21:12 PM
 */
@SuppressWarnings("unchecked")
public class ResponseHeadSettingFilter implements Filter {
	FilterConfig fc;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.fc = filterConfig;
	}

	public void destroy() {
		this.fc = null;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		if (fc != null)
			for (Enumeration e = fc.getInitParameterNames(); e
					.hasMoreElements();) {
				String headerName = (String) e.nextElement();
				response.setHeader(headerName, fc.getInitParameter(headerName));
				// System.out.println("head:" + headerName + ",value:" +
				// fc.getInitParameter(headerName));
				// System.out.println("url:" + ((HttpServletRequest)
				// req).getRequestURL());
			}

		chain.doFilter(req, response);
		
	}
}
