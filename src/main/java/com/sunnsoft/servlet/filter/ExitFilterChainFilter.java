package com.sunnsoft.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ExitFilterChainFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// System.out.println("filterExit");
		HttpServletRequest request = (HttpServletRequest) req;
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String uriWithoutContextPath = uri.substring(contextPath.length());
		request.getRequestDispatcher(uriWithoutContextPath).forward(req, resp);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
	}

}
