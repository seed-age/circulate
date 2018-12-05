package com.sunnsoft.sloa.actions.app.create;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.util.struts2.Results;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取联系人信息的测试接口(用于测试)
 *
 * @author chenjian
 *
 */
public class FindUserMssage extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "用户ID不能为空!");

		String SQL = "SELECT DISTINCT r.user_id userId, r.department_name departmentName, r.subcompany_name fullName, r.last_name lastName FROM receive_tbl r WHERE r.re_differentiate = " + userId + " AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(r.receive_time) GROUP BY r.user_id ORDER BY r.receive_time DESC;";
		List<Map<String, Object>> mapList1 = jdbcTemplate.queryForList(SQL);

		json = JSONObject.toJSONString(mapList1);
		success = true;
		code = "200";
		msg = "获取最近联系人成功!";
		return Results.GLOBAL_FORM_JSON;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
