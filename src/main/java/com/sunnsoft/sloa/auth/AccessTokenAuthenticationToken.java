package com.sunnsoft.sloa.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AccessTokenAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private final Object principal;

	public AccessTokenAuthenticationToken(Object principal,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);

		if ((principal == null) || ("".equals(principal))) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
		}

		this.principal = principal;
		setAuthenticated(true);
	}

	public Object getCredentials() {
		return "";
	}

	public Object getPrincipal() {
		return this.principal;
	}

}
