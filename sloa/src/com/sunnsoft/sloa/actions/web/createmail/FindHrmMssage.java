package com.sunnsoft.sloa.actions.web.createmail;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.bizz.UserMssageSql;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.util.struts2.Results;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * 新建传阅---点击右侧联系人和常用组: 查询 分部信息, 部门信息, 人员信息展示到前端.(初始版)
 * 
 * @author chenjian
 *
 */
public class FindHrmMssage extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private int statusHrm; // 0 为 分部  1 为 部门  2 为 人员
	private String subcompanyid; // 分部ID
	private String departmentid; // 部门ID

	@Resource
	private UserMssageSql userMssageSql;
	
	@Override
	public String execute() throws Exception {

		try {
			
			switch (statusHrm) {
			case 0: 	//分部
				// 1. 查询出分部信息, 并且展示到前端
				List<Map<String, Object>> list = userMssageSql.getFullNameOrSubcompanyId1();
				
				// 转换成JSON格式对象
				json = JSONObject.toJSONString(list);
				if (json != null) {
					success = true;
					code = "200";
					msg = "获取分部信息成功!";
					return Results.GLOBAL_FORM_JSON;
				}
				break;

			case 1: //部门
				// 2. 查询出部门信息, 并且展示到前端
				List<Map<String,Object>> deptList = userMssageSql.getDepartmentNameOrId(subcompanyid);
				
				// 转换成JSON格式对象
				json = JSONObject.toJSONString(deptList);
				if (json != null) {
					success = true;
					code = "200";
					msg = "获取部门信息成功!";
					return Results.GLOBAL_FORM_JSON;
				}
				break;
			case 2: //人员
				// 2. 查询出部门下的人员信息, 并且展示到前端   0：试用 1：正式 2：临时 3：试用延期 4：解聘 5：离职 6：退休 7：无效
				json = Services.getUserMssageService().createHelper().getDepartmentId().Eq(departmentid).getStatus().Le("3").json().includeUserId().includeLastName().includeDepartmentId().listJson();
				if (json != null) {
					success = true;
					code = "200";
					msg = "获取部门下的人员信息成功!";
					return Results.GLOBAL_FORM_JSON;
				}
				break;
			default:
				success = false;
				code = "4000";
				msg = "网络繁忙,请稍后再试!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			code = "4000";
			msg = "网络繁忙,请稍后再试!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		
		success = false;
		code = "404";
		msg = "获取数据失败!";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	public int getStatusHrm() {
		return statusHrm;
	}

	public void setStatusHrm(int statusHrm) {
		this.statusHrm = statusHrm;
	}

	public String getSubcompanyid() {
		return subcompanyid;
	}

	public void setSubcompanyid(String subcompanyid) {
		this.subcompanyid = subcompanyid;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
}
