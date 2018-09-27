package com.sunnsoft.util.struts2;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
* 防止xss和sql注入攻击的过滤器。
* 
在struts.xml,struts-deploy.xml需要配置本拦截器。
 <interceptor name="escapeParameters" class="com.sunnsoft.util.struts2.EscapeParametersInterceptor"/>
配置拦截器：  
 <interceptor-ref name="escapeParameters">
   <param name="filterTypes">sql,html</param>
 </interceptor-ref>
 	
 或，独立使用到本拦截器的action注解：
 	@Override
	@Action(interceptorRefs = {
			@InterceptorRef(value = "escapeParamters"),
			@InterceptorRef("extStack") 
			}
	)
*/

public class EscapeParametersInterceptor extends AbstractInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9035604438776170227L;
	
	private Set<String> filterSet = new HashSet<String>();


	public void setFilterTypes(String filterTypes){
		String[] strs = StringUtils.deleteWhitespace(filterTypes).split(",");
		for (int i = 0; i < strs.length; i++) {
			this.filterSet.add(strs[i]);
		}
	}
	

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		ActionContext actionContext = arg0.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(StrutsStatics.HTTP_REQUEST);

		Iterator iter = request.getParameterMap().values().iterator();
		while (iter.hasNext()) {
			String[] parames = (String[]) iter.next();
			for (int i = 0; i < parames.length; i++) {
				if(filterSet.contains("html")){
					parames[i] = HtmlUtils.htmlEscape(parames[i] );
				}
				if(filterSet.contains("sql")){
					parames[i] = StringEscapeUtils.escapeSql(parames[i] );
				}
			}
		}
		return arg0.invoke();
	}

}