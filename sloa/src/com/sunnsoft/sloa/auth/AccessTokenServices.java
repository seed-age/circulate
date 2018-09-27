package com.sunnsoft.sloa.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessTokenServices implements InitializingBean {
	
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private TokenUserDetailsService tokenUserDetailsService;
	private String tokenName;
	
	private boolean checkFromHeader = true;
	
	private boolean checkFromRequestParameter ;
	
	protected final Log logger = LogFactory.getLog(AccessTokenServices.class);
	
	public AccessTokenServices(TokenUserDetailsService tokenUserDetailsService , String tokenName) {
		super();
		this.tokenUserDetailsService = tokenUserDetailsService;
		this.tokenName = tokenName;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
	public boolean isCheckFromHeader() {
		return checkFromHeader;
	}

	public void setCheckFromHeader(boolean checkFromHeader) {
		this.checkFromHeader = checkFromHeader;
	}

	public boolean isCheckFromRequestParameter() {
		return checkFromRequestParameter;
	}

	public void setCheckFromRequestParameter(boolean checkFromRequestParameter) {
		this.checkFromRequestParameter = checkFromRequestParameter;
	}

	public void setTokenUserDetailsService(TokenUserDetailsService tokenUserDetailsService) {
		this.tokenUserDetailsService = tokenUserDetailsService;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}
	
	public final Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = null;
		
		if(this.checkFromHeader){
			accessToken = request.getHeader(tokenName);
		}
		if( accessToken == null &&this.checkFromRequestParameter){
			accessToken = request.getParameter(tokenName);
		}
		if(accessToken == null ){
			return null;
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("access token detected");
		}
		if (accessToken.length() == 0) {
			this.logger.debug("access token was empty");
			return null;
		}

		UserDetails user = null;
		try {
			
			user = this.tokenUserDetailsService.loadUserByAccessToken(accessToken);
			this.userDetailsChecker.check(user);

			this.logger.debug("access token accepted");

			return createSuccessfulAuthentication(request, user);
		}  catch (UsernameNotFoundException noUser) {
			this.logger.debug("user not found");
			return null;
		}  catch (AccountStatusException statusInvalid) {
			this.logger.debug("Invalid UserDetails: " + statusInvalid.getMessage());
		} 

		return null;
	}
	
	protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
		AccessTokenAuthenticationToken auth = new AccessTokenAuthenticationToken(user,
				user.getAuthorities());
		auth.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return auth;
	}

	public void afterTokenAccessAuthenticateionSuccess(HttpServletRequest request) {
		String accessToken = request.getParameter(tokenName);
		this.tokenUserDetailsService.afterAuthenticateSuccess(accessToken);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.authenticationDetailsSource,"authenticationDetailsSource can't be null");
		Assert.notNull(this.tokenName,"tokenName can't be null");
		Assert.notNull(this.tokenUserDetailsService,"tokenUserDetailsService can't be null");
		Assert.notNull(this.userDetailsChecker,"userDetailsChecker can't be null");
	}

}
