package com.sunnsoft.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author llade
 *
 * 过滤掉session
 * 对于webservice等不需要用到session的应用，可以过滤掉，避免占用内存
 */

public class NoSessionFilter implements Filter {

	private static class RequestWrapper extends HttpServletRequestWrapper {

		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public HttpSession getSession() {
			return null;
		}

		@Override
		public HttpSession getSession(boolean create) {
			return null;
		}
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest) request), response); 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
