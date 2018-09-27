package com.sunnsoft.servlet.filter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * 
 * @author llade
 *
 * 性能检测以及访问日志
 */

@SuppressWarnings("unchecked")
public class ProfileFilter implements Filter {

	boolean logAgent = true;
	boolean logReferer = true;
	boolean logProfileTime = true;
	boolean logParams = true;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String uuid = null;
		Long start = 0l;
		if(logProfileTime){
			uuid = UUID.randomUUID().toString();
			start = System.nanoTime();
		}
		if(uuid != null){
			uuid = " [" + uuid+"] "; 
		}else{
			uuid = "";
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = null;
		if(auth != null){
			user = auth.getName();
		}
		if(log.isInfoEnabled()){
			log.info(uuid + " ip : [" + req.getRemoteAddr() + "]" );
			log.info(uuid + " user : [" + user + "]" );
			log.info(uuid + " uri : [" + req.getRequestURI() + "]" );
			log.info(uuid + " method : [" + req.getMethod() + "]" );
		}
		if(logParams){
			StringBuilder sb = new StringBuilder();
			Enumeration params = req.getParameterNames();
			while(params.hasMoreElements()){
				String param = (String)params.nextElement();
				String[] values = req.getParameterValues(param);
				sb.append( param ).append(" => ").append(ArrayUtils.toString(values)).append(" ");
			}
			log.info(uuid + " param : [" + sb + "]");
		}
		if(logAgent){
			if(log.isInfoEnabled())log.info(uuid + " browser : [" + req.getHeader("user-agent") + "]" );
		}
		if(logReferer){
			if(log.isInfoEnabled())log.info(uuid + " referer : [" + req.getHeader("referer")+ "]" );
		}
		
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			
			if(log.isInfoEnabled())log.info(uuid + " request error : ", e);
			if(e instanceof IOException){
				throw (IOException)e;
			}else if(e instanceof ServletException){
				throw (ServletException) e;
			}else{
				throw new ServletException(e);
			}
		}finally{  
			if(logProfileTime){
				if(log.isInfoEnabled())log.info(uuid+" request-cose : [" + ((System.nanoTime()-start)/1000/1000) + "] ms");
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		logAgent = !"false".equalsIgnoreCase(config.getInitParameter("logAtent"));
		logReferer = !"false".equalsIgnoreCase(config.getInitParameter("logReferer"));
		logProfileTime = !"false".equalsIgnoreCase(config.getInitParameter("logProfileTime"));
		logParams = !"false".equalsIgnoreCase(config.getInitParameter("logParams"));
	}

}
