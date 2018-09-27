package com.sunnsoft.util.struts2;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * 从拦截器获取spring的ApplicationContext
 * @author llade
 *
 */
public abstract class SpringApplicationContextInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected ApplicationContext appContext;
	@Inject  
	public void setServletContext(ServletContext servletContext) {
		this.appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}  


}
