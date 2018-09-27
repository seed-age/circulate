package com.sunnsoft.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
