package com.sunnsoft.sloa.auth;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.User;



public class OnlyOnceTokenUserDetailsService implements TokenUserDetailsService {
	

	public OnlyOnceTokenUserDetailsService() {
		super();
	}

	public UserDetails loadUserByAccessToken(String accessToken) throws UsernameNotFoundException, DataAccessException {
		User user = null;
		try {
			user = Services.getUserService().createHelper().getAccessToken().Eq(accessToken).getAccessTokenExpire()
					.Ge(new Date()).uniqueResult();// 令牌正确且尚未失效
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		user.setLastLogin(new Date());// 设置最后登录时间
		Services.getUserService().update(user);

		return new SystemUser(user);
	}

	
	/**
	 * 通常access token 是一次性。用完就需要清掉。但有些情况特殊，如果是需要长期保存使用的token，则需要在本方法更新最新的过期日期为最新的日期。
	 */
	@Override
	public void afterAuthenticateSuccess(String accessToken) {
		Services.getUserService().createHelper().getAccessToken().Eq(accessToken).bean().setAccessToken(null)
				.setAccessTokenExpire(null).update();
	}

}
