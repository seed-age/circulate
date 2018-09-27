package com.sunnsoft.sloa.util;


import com.opensymphony.xwork2.ActionContext;
import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.db.handler.Services;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

//@SuppressWarnings("unchecked")
public class UserUtils {
	
	/**
	 * 获取当前用户
	 * @return
	 */
	public static SystemUser getCurrentUser(){
		return (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * 一般网站注册后可以直接登录，可以调用此方法传入注册时候的用户名和密码实现登录，但不启用remember me 功能
	 * @param username
	 * @param password
	 */
	public static void autoLogin(String username,String password){
		autoLogin(username,password,null);
	}
	/**
	 * 一般网站注册后可以直接登录，可以调用此方法传入注册时候的用户名和密码实现登录，并且启用remember me 功能,需要传入相应的rememberMeServices Bean Id
	 * @param username
	 * @param password
	 * @param rememberMeServicesId
	 */
	public static void autoLogin(String username,String password,String rememberMeServicesId){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, password);

        
        token.setDetails(new WebAuthenticationDetails(ServletActionContext.getRequest()));
        Authentication authenticatedUser =((AuthenticationManager)Services.getUserService().getBean("authenticationManager"))
                .authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        ActionContext.getContext().getSession().put(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        
        if(rememberMeServicesId != null){
        	RememberMeServices rememberMeServices = (RememberMeServices)Services.getUserService().getBean(rememberMeServicesId);
        	rememberMeServices.loginSuccess(ServletActionContext.getRequest(), ServletActionContext.getResponse(), authenticatedUser);
        }
	}
	
	
}
