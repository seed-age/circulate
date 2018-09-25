package com.sunnsoft.sloa.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.User;



public class LongPeriodTokenUserDetailsService implements TokenUserDetailsService {
	
	
	protected Log logger = LogFactory.getLog(getClass());
	
	
	private int tokenValiditySeconds = 1209600 ;//两周
	
	public int getTokenValiditySeconds() {
		return tokenValiditySeconds;
	}

	public void setTokenValiditySeconds(int tokenValiditySeconds) {
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	public LongPeriodTokenUserDetailsService() {
		super();
	}

	public UserDetails loadUserByAccessToken(String accessToken)
			throws UsernameNotFoundException, DataAccessException {
		
		User user = null;
		try {
			user = Services.getUserService().createHelper().getAccessToken().Eq(accessToken).getAccessTokenExpire().Ge(new Date()).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}

		return new SystemUser(user);
	}

	/**
	 * 刷新token过期时间
	 */
	@Override
	public void afterAuthenticateSuccess(String accessToken) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, tokenValiditySeconds);
		List<User> users = Services.getUserService().createHelper().getAccessToken().Eq(accessToken).bean()
				.setAccessTokenExpire(cal.getTime()).update();
		
		if(users == null || users.isEmpty()){
			logger.warn("access token无对应的账号对象");
		}else if(users.size() > 1){
			logger.warn("access token 找到多个个账号对象 ,access token:" + accessToken);
			for (User user : users) {
				logger.warn("access token 找到多个个账号对象 , 账号id: " + user.getAccountName());
			}
		}
	}

}
