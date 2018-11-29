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

//		String SQL = "SELECT * FROM user_mssage_tbl u WHERE u.user_id IN(SELECT r.user_id FROM receive_tbl r WHERE r.re_differentiate = " + userId + " AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(r.receive_time) GROUP BY r.last_name)";

//		String SQL = "SELECT r.user_id FROM receive_tbl r WHERE r.re_differentiate = " + userId + " AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(r.receive_time) GROUP BY r.last_name";
//		List<Map<String, Object>> mapList = jdbcTemplate.queryForList(SQL);

		String SQL1 = "SELECT DISTINCT r.user_id userId, r.department_name departmentName, r.subcompany_name fullName, r.last_name lastName FROM receive_tbl r WHERE r.re_differentiate = " + userId + " AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(r.receive_time) GROUP BY r.receive_time DESC;";
		List<Map<String, Object>> mapList1 = jdbcTemplate.queryForList(SQL1);
//		System.out.println(JSONObject.toJSONString(mapList1));
//
//		// 存储联系人对象
//		List<UserMssage> listHrm = new ArrayList<UserMssage>();
//		for(Map<String, Object> map : mapList){
//			Long user_id = (Long)map.get("user_id");
//			UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq(user_id.intValue()).uniqueResult();
//			listHrm.add(mssage);
//		}

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
