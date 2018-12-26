package com.sunnsoft.sloa.actions.web.createmail;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 新建传阅---添加联系人--点击同部门: 查询出该用户所在部门的所有人员信息.
 * @author chenjian
 *
 */
public class FindHrmSameDepartment extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "用户ID不能为空!");

		// 根据用户ID查询该用户所在的部门
		UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue())
				.uniqueResult();

		// 获取该用户所在的部门ID
		String departmentId = userMssage.getDepartmentId();
		// 获取该用户所在的部门名称
		String deptFullname = userMssage.getDeptFullname();

		// 根据该用户所在的部门ID 和 部门名称, 查询出该用户同一个部门的所有人员信息
		json = Services.getUserMssageService().createHelper().getDepartmentId().Eq(departmentId).getDeptFullname()
				.Eq(deptFullname).getStatus().Le("3").json().listJson(new EachEntity2Map<UserMssage>() {
					@Override
					public void each(UserMssage userMssage, Map<String, Object> map) {
						map.put("type", "userMssage");
					}
				});

		success = true;
		code = "200";
		msg = "查询当前用户同部门信息成功!";
		return Results.GLOBAL_FORM_JSON;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
