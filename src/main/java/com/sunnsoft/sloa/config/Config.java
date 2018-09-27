package com.sunnsoft.sloa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {


	/**
	 * 是否开启extjs debug模式，在此模式下，会显示和debug相关的按钮，例如右上角"api"按钮
	 */
	private @Value("${ext.debug}") boolean extDebug;
	
	/**
	 * 登录时候加密用户名和密码使用的加密和解密key
	 */
	private @Value("${login.encrypt.public.key}") String loginPublicKey ;
	
	/**
	 * 网盘的账号 、密码、地址、存储上传文件的路径(生产环境)
	 */
	private @Value("${oa.box.userslug}") String userSlug;
	private @Value("${oa.box.password}") String password;
	private @Value("${oa.box.url}") String boxUrl;
	private @Value("${oa.box.upload.url}") String boxUploadUrl;
	
	public boolean isExtDebug() {
		return extDebug;
	}

	public String getLoginPublicKey() {
		return loginPublicKey;
	}

	public String getUserSlug() {
		return userSlug;
	}

	public String getPassword() {
		return password;
	}

	public String getBoxUrl() {
		return boxUrl;
	}

	public String getBoxUploadUrl() {
		return boxUploadUrl;
	}
}
