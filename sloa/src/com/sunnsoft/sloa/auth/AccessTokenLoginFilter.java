package com.sunnsoft.sloa.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;


public class AccessTokenLoginFilter extends GenericFilterBean implements ApplicationEventPublisherAware {
	
	private ApplicationEventPublisher eventPublisher;
	private AuthenticationSuccessHandler successHandler;
	private AuthenticationManager authenticationManager;
	private AccessTokenServices accessTokenServices;
	private RememberMeServices rememberMeServices = new NullRememberMeServices();
	
	public AccessTokenLoginFilter(AuthenticationManager authenticationManager,
			AccessTokenServices accessTokenServices) {
		super();
		this.authenticationManager = authenticationManager;
		this.accessTokenServices = accessTokenServices;
	}

	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		Assert.notNull(this.authenticationManager, "authenticationManager must be specified");
		Assert.notNull(this.accessTokenServices, "accessTokenServices must be specified");
		super.afterPropertiesSet();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Authentication accessTokenAuth = this.accessTokenServices.autoLogin(request, response);

			if (accessTokenAuth != null) {
				try {
					accessTokenAuth = this.authenticationManager.authenticate(accessTokenAuth);

					SecurityContextHolder.getContext().setAuthentication(accessTokenAuth);

					onSuccessfulAuthentication(request, response, accessTokenAuth);

					if (this.logger.isDebugEnabled()) {
						this.logger.debug("SecurityContextHolder populated with access token: '"
								+ SecurityContextHolder.getContext().getAuthentication() + "'");
					}

					if (this.eventPublisher != null) {
						this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
								SecurityContextHolder.getContext().getAuthentication(), super.getClass()));
					}

					if (this.successHandler != null) {
						this.successHandler.onAuthenticationSuccess(request, response, accessTokenAuth);

						return;
					}
				} catch (AuthenticationException authenticationException) {
					if (this.logger.isDebugEnabled()) {
						this.logger.debug(
								"SecurityContextHolder not populated with access token, as AuthenticationManager rejected Authentication returned by AccessTokenServices: '"
										+ accessTokenAuth + "'; invalidating access token",
								authenticationException);
					}

					onUnsuccessfulAuthentication(request, response, authenticationException);
				}
			}

		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger
						.debug("SecurityContextHolder not populated with access token, as it already contained: '"
								+ SecurityContextHolder.getContext().getAuthentication() + "'");
			}
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * 登录不成功需要做的动作，可扩展
	 * @param request
	 * @param response
	 * @param authenticationException
	 */
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) {
	}
	
	/**
	 * 登录成功需要做的动作，可扩展
	 * @param request
	 * @param response
	 * @param accessTokenAuth
	 */
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication accessTokenAuth) {
		
		rememberMeServices.loginSuccess(request, response, accessTokenAuth);
		
		this.accessTokenServices.afterTokenAccessAuthenticateionSuccess(request);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

}
