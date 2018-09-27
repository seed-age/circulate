package com.sunnsoft.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.springframework.util.AntPathMatcher;

/**
 * 
 * @author llade
 * @version 1.0 Creation date: Oct 9, 2008 10:31:32 AM
 */
public class OpenSessionInViewFilterDelegate extends OpenSessionInViewFilter {

	protected String[] mappings = new String[] {};
	protected String[] exclude = new String[] {};
	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	public String[] getExclude() {
		return exclude;
	}

	public void setExclude(String[] exclude) {
		this.exclude = exclude;
	}

	private boolean filterURI(String uriWithoutContextPath) {
		for (int i = 0; i < exclude.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("test exclude, pattern:" + exclude[i] + ",uri:"
						+ uriWithoutContextPath);
			}
			if (antPathMatcher.match(exclude[i], uriWithoutContextPath)) {
				if (logger.isDebugEnabled()) {
					logger.debug("excluded, pattern:" + exclude[i] + ",uri:"
							+ uriWithoutContextPath);
				}
				return false;
			}
		}

		for (int i = 0; i < mappings.length; i++) {
			if (logger.isDebugEnabled()) {
				logger.debug("matching,pattern:" + mappings[i] + ",uri:"
						+ uriWithoutContextPath);
			}
			if (antPathMatcher.match(mappings[i], uriWithoutContextPath)) {
				if (logger.isDebugEnabled()) {
					logger.debug("matched,pattern:" + mappings[i] + ",uri:"
							+ uriWithoutContextPath);
				}
				return true;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("not matched,uri:" + uriWithoutContextPath);
		}
		return false;
	}

	public String[] getMappings() {
		return mappings;
	}

	public void setMappings(String[] mappings) {
		this.mappings = mappings;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		long start = System.currentTimeMillis();
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String uriWithoutContextPath = uri.substring(contextPath.length());
		if (this.filterURI(uriWithoutContextPath)) {
			super.doFilterInternal(request, response, filterChain);
		} else {
			filterChain.doFilter(request, response);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("time cost:" + (System.currentTimeMillis() - start));
		}
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		for (int i = 0; i < mappings.length; i++) {
			mappings[i] = mappings[i].trim();
		}
		for (int i = 0; i < exclude.length; i++) {
			exclude[i] = exclude[i].trim();
		}
		super.afterPropertiesSet();
	}

}
