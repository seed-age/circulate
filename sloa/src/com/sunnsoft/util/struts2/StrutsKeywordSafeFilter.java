package com.sunnsoft.util.struts2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * @author llade
 * 
 *  struts2的参数中，带有特殊的字符会由于ONGL表达式引起严重的系统漏洞。容易被黑客攻击。
 *  此filter是过滤这些特殊的关键字，参数名有该关键字的，将被屏蔽。
 *  对于无法升级的来解决的漏洞，或者来不及升级的漏洞，此filter相当有用。
 */

public class StrutsKeywordSafeFilter implements Filter {

	/**从过往版本的struts-default.xml，以及安全论坛中提取的关键字正则表达式。
	 */
	private static final String[] unsafeWord = { "dojo\\..*", "^struts\\..*",
			"^session\\..*", "^request\\..*", "^application\\..*",
			"^servlet(Request|Response)\\..*", "parameters\\...*",
			".*\\\\u0023.*", ".*\u0023.*", ".*redirect:.*",
			".*redirectAction:.*", ".*action:.*" };

	private static class RequestWrapper extends HttpServletRequestWrapper {

		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getParameter(String name) {
			if (!testSate(name))
				return null;
			return super.getParameter(name);
		}

		@Override
		public Map getParameterMap() {
			Map map = new HashMap();
			map.putAll(super.getParameterMap());
			for (Iterator iterator = map.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();

				if (!testSate(key)) {
					iterator.remove();
				}

			}
			return map;
		}

		@Override
		public Enumeration getParameterNames() {
			Enumeration e = super.getParameterNames();
			List array = new ArrayList();
			while (e.hasMoreElements()) {
				String param = (String) e.nextElement();
				if (testSate(param)) {
					array.add(param);
				}
			}

			final Iterator i = array.iterator();
			Enumeration e2 = new Enumeration() {

				Iterator iter = i;

				@Override
				public boolean hasMoreElements() {
					return iter.hasNext();
				}

				@Override
				public Object nextElement() {
					return iter.next();
				}

			};
			return e2;
		}

		@Override
		public String[] getParameterValues(String name) {
			if (!testSate(name))
				return null;
			return super.getParameterValues(name);
		}

	}

	private static boolean testSate(String input) {
		for (int i = 0; i < unsafeWord.length; i++) {
			if (Pattern.matches(unsafeWord[i], input)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new RequestWrapper((HttpServletRequest) request),
				response);
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {

	}

}
