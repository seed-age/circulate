package com.sunnsoft.sloa.actions.web.createmail;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.util.struts2.Results;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;


import javax.annotation.Resource;
import java.util.*;

/**
 * 新建传阅--添加联系--点击最近: 查询出该用户在新建传阅中添加联系人时, 七天以内添加联系人的数据.
 *
 * @author chenjian
 *
 */
public class FindLatelyHrm extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 当前用户ID

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public String execute() throws Exception {

		// 参数校验
		Assert.notNull(userId, "用户ID不能为空!");

		String SQL1 = "select u.user_id userId, u.department_id departmentId, u.full_name fullName, u.dept_fullname deptFullname, u.last_name lastName, 'userMssage' as type from user_mssage_tbl u where u.user_id in (select * from (select distinct r.user_id from receive_tbl r where r.re_differentiate = " + userId + " order by r.receive_time desc LIMIT 200) as x)";
		List<Map<String, Object>> mapList1 = jdbcTemplate.queryForList(SQL1);

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
