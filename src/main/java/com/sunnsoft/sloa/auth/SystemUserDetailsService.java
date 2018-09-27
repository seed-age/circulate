package com.sunnsoft.sloa.auth;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.helper.UserHelper;
import com.sunnsoft.sloa.service.UserService;

public class SystemUserDetailsService implements UserDetailsService,
		InitializingBean {

	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public SystemUserDetailsService(UserService userService) {
		super();
		this.userService = userService;
	}

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		User userVo = null;
		try {
			userVo = new UserHelper(this.userService).getAccountName().Eq(userName).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (userVo == null) {
			throw new UsernameNotFoundException("user not found");
		}
		userVo.setLastLogin(new Date());//设置最后登录时间
		this.userService.update(userVo);

		return new SystemUser(userVo);
	}
	

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userService,"userService 不能为空");
	}

}
