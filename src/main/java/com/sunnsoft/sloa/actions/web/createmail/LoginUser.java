package com.sunnsoft.sloa.actions.web.createmail;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;

/**
 * 获取登陆用户的loginId,  再根据loginId获取用户信息
 * @author chenjian
 *
 */
public class LoginUser extends BaseParameter {

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		//根据SSO登录获取当前用户的session
		String loginId = LenovoCloudSDKUtils.getLoginId();

		//String loginId = "zhangluoyun";
		loginId = "limin5";
		// 根据loginId查询用户信息
		json = Services.getUserMssageService().createHelper().getLoginId().Eq(loginId).json().uniqueJson();

		// 判断, 如果不为空的话 就是登录成功, 为空就是登录失败
		if (json != null) {
			success = true;
			code = "200";
			msg = "获取用户ID成功!";
			return Results.GLOBAL_FORM_JSON;
		}
		success = false;
		code = "404";
		msg = "获取用户ID失败!";
		return Results.GLOBAL_FORM_JSON;
	}
}
