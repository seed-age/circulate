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

/**
 * 
 * @author llade
 *
 * 获取用户浏览器的所属真实的ip地址，主要是避免经过代理服务器后，getRemoteAddr()获取的IP地址是代理服务器地址。
 * 对于有代理服务器的情况下。要用次过滤器过滤所有请求。
 * 
 * 首选是设置tomcat，server.xml在Host标签加入:
 * <Valve
        className="org.apache.catalina.valves.RemoteIpValve"
        remoteIpHeader="x-forwarded-for"
        protocolHeader="x-forwarded-proto"
      />
 *  其次，如果无法设置tomcat，才使用本filter。
 * 
 */

public class BrowserIpExtractorFilter implements Filter {

	private static class RequestWrapper extends HttpServletRequestWrapper {
		
		HttpServletRequest request;
		
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getRemoteAddr() {
			String ip = request.getHeader("x-forwarded-for"); 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getHeader("Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getHeader("WL-Proxy-Client-IP"); 
		    } 
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		        ip = request.getRemoteAddr(); 
		    } 
		    if( ip.indexOf(",")!= -1){
		    	ip = ip.split(",")[0];
		    }
			return ip;
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
