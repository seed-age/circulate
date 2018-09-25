package com.sunnsoft.util.struts2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class EncodingInterceptor extends AbstractInterceptor {


	/**
	 * Struts2 get编码拦截器，客户端参数需要用encodeURI进行2次编码。
	 * 如果用jquery的form serialize方式，则只需要编码多一次就可以了。因为form serialize已经编码一次了。
	 * 以下是jsonp跨域提交例子。
	 *$.ajax({
        url:'http://localhost:8080/gdct1/gz/dkd/submit-order.htm',
        type:'get',
        data:encodeURI($('#form1').serialize()),
        dataType:"jsonp",
        jsonp:"callback",
        success:function(data){
        }
      });
      而struts.xml,struts-deploy.xml需要配置本拦截器。
      <interceptor name="encoding" class="com.sunnsoft.util.struts2.EncodingInterceptor"/>
      使用到本拦截器的action需要注解：
      	@Override
		@Action(interceptorRefs = {
				@InterceptorRef(value = "encoding"),
				@InterceptorRef("extStack") 
				}
		)
	 */

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {

		ActionContext actionContext = arg0.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) actionContext
				.get(StrutsStatics.HTTP_REQUEST);

		Iterator iter = request.getParameterMap().values().iterator();
		while (iter.hasNext()) {
			String[] parames = (String[]) iter.next();
			for (int i = 0; i < parames.length; i++) {
				try {
					parames[i] = URLDecoder.decode(parames[i], "UTF-8");// 此处GBK与页面编码一样
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return arg0.invoke();
	}

}