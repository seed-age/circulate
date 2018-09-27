package com.sunnsoft.sloa.auth;


import com.sunnsoft.sloa.db.vo.User;
import org.gteam.constants.RoleConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SystemUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(2);
	private User userVo;

	public SystemUser(User vo) {
		super();
		this.authorities.add(new SimpleGrantedAuthority(RoleConstants.ROLE_USER))
		;
//		System.out.println(this.authorities);
		this.userVo = vo;
		 if(userVo.isAdmin()){
			 this.authorities.add(new SimpleGrantedAuthority(RoleConstants.ROLE_SYS_ADMIN))
				;
		 }
	}

	

	public String getPassword() {
		return this.userVo.getUserPassword();
	}

	public String getUsername() {
		return this.userVo.getAccountName();
	}

	public Long getUserId() {
		return this.userVo.getUserId();
	}

	 public boolean isAdmin() {
		return this.userVo.isAdmin();
	 }

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return Boolean.TRUE.equals(userVo.isEnabled());
	}

	public User getUserVo() {
		return userVo;
	}

	public void setUserVo(User userVo) {
		this.userVo = userVo;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

}
