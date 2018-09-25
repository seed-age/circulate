package com.sunnsoft.sloa.auth;

public interface TokenAuthenticationSuccess {

	/**
	 * 通常access token 是一次性。用完就需要清掉。但有些情况特殊，如果是需要长期保存使用的token，则需要在本方法更新最新的过期日期为最新的日期。
	 * @param accessToken
	 */
	void afterAuthenticateSuccess(String accessToken);
	
}
