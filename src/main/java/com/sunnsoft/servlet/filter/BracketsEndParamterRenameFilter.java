package com.sunnsoft.servlet.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * @author 林宇民 Andy (llade)
 * 对于提交的数组参数里，有以[]结尾的参数名，此类需要去掉中括号，需要此filter过滤
 *
 */
public class BracketsEndParamterRenameFilter implements Filter {

	
	private class RequestWrapper extends HttpServletRequestWrapper {
		
		HttpServletRequest request;
		
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			if(name != null && !name.endsWith("[]")) {
				return request.getParameter(name);
			}
			String[] values = this.getParameterMap().get(name);
			if(values != null && values.length > 0 ) {
				return values[0];
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequestWrapper#getParameterMap()
		 */
		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> oldMap = request.getParameterMap();
			if(oldMap.isEmpty()) {
				return oldMap;
			}
			Map<String, String[]> newMap = new LinkedHashMap<String, String[]>();
			boolean changed = false;
			for (Map.Entry<String, String[]> entry : oldMap.entrySet()) {
				String key = entry.getKey();
				if(key .endsWith("[]")) {
					key = key.substring(0,key.length()-2);
					changed = true;
				}
				newMap.put(key, entry.getValue());
			}
			return changed?newMap : oldMap;
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequestWrapper#getParameterNames()
		 */
		@Override
		public Enumeration<String> getParameterNames() {
			Vector<String> v = new Vector<String>();
			v.addAll(this.getParameterMap().keySet());
			return v.elements();
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
		 */
		@Override
		public String[] getParameterValues(String name) {
			return this.getParameterMap().get(name);
		}
		
		
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest) req), res);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {

	}

}
