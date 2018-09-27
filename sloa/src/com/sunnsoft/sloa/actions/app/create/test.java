package com.sunnsoft.sloa.actions.app.create;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;

import javax.annotation.Resource;

public class test extends BaseParameter {
	private static final long serialVersionUID = 1L;

	@Resource
	private Config config;
	
	@Override
	public String execute() throws Exception {
		
		LenovoCloudSDK lenovoCloudSDK = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		String systemOAsession = LenovoCloudSDKUtils.getLenovoCloudSDKSession(lenovoCloudSDK, config);
		System.out.println(systemOAsession);
		msg = systemOAsession;
		return Results.GLOBAL_SUCCESS_JSON;
	}
}
