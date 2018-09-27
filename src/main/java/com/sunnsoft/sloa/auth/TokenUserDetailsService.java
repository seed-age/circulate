package com.sunnsoft.sloa.auth;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author 林宇民 Andy (llade)
 *
 */
public interface TokenUserDetailsService extends TokenAuthenticationSuccess{
	
	UserDetails loadUserByAccessToken(String accessToken) throws UsernameNotFoundException, DataAccessException;
	
}
