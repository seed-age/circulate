package com.sunnsoft.sloa.auth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

public class AccessTokenAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public AccessTokenAuthenticationProvider() {
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.messages, "A message source must be set");
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!(supports(authentication.getClass()))) {
			return null;
		}

		return authentication;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	public boolean supports(Class<?> authentication) {
		return AccessTokenAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
