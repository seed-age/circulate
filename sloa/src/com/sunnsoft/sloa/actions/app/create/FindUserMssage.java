package com.sunnsoft.sloa.actions.app.create;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.schedule.HrmUserSchedule;
import com.sunnsoft.util.struts2.Results;

import javax.annotation.Resource;

/**
 * 获取联系人信息的测试接口(用于测试)
 * 
 * @author chenjian
 *
 */
public class FindUserMssage extends BaseParameter {

	private static final long serialVersionUID = 1L;

	@Resource
	private HrmUserSchedule ss;

	@Override
	public String execute() throws Exception {

		// 查询所有
		json = Services.getUserMssageService().createHelper().getDeptFullname().Eq("营销管理中心").json().listJson();
		success = true;
		code = "200";
		msg = "获取人员信息成功!";
		return Results.GLOBAL_FORM_JSON;
	}

}
