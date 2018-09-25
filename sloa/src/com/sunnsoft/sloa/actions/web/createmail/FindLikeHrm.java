package com.sunnsoft.sloa.actions.web.createmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.util.PageUtils;
import com.sunnsoft.util.struts2.Results;


/**
 * 新建传阅 -- 添加联系人--模糊搜索联系人(接收人姓名)
 * 
 * @author chenjian
 *
 */
public class FindLikeHrm extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private String likeName; //用户输入的

	private Integer page; // 当前页
	private Integer pageRows; // 显示几条记录
	
	@Override
	public String execute() throws Exception {

		//校验参数
		Assert.notNull(likeName , "模糊查询的条件不能为空!");
		
		// 设置默认分页参数
		page = PageUtils.defaultPageNumber(page); // 1
		pageRows = PageUtils.defaultPageSize(pageRows); // 10
		
		try {
			//查询
			List<UserMssage> list = Services.getUserMssageService().createHelper().getLastName().Like(likeName).list();
			 
			List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
			 
			for (UserMssage userMssage : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userMssage.getUserId());
				map.put("workCode", userMssage.getWorkCode());
				map.put("lastName", userMssage.getLastName());
				map.put("loginId", userMssage.getLoginId());
				map.put("subcompanyId1", userMssage.getSubcompanyId1());
				map.put("departmentId", userMssage.getDepartmentId());
				map.put("fullName", userMssage.getFullName());
				map.put("deptFullname", userMssage.getDeptFullname());
				userList.add(map);
				
				//只需要前十条数据
				if(userList.size() == pageRows) {
					break;
				}
			}
			
			json = JSONObject.toJSONString(userList);
			
			if (json != null) {
				success = true;
				code = "200";
				msg = "模糊查询人员信息成功!";
				return Results.GLOBAL_FORM_JSON;
			}else {
				success = false;
				code = "404";
				msg = "模糊查询失败!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			success = false;
			code = "4000";
			msg = "网络繁忙,请稍后再试!";
			json = "null";
			e.printStackTrace();
			return Results.GLOBAL_FORM_JSON;
		}
		
	}

	
	public Integer getPage() {
		return page;
	}


	public void setPage(Integer page) {
		this.page = page;
	}


	public Integer getPageRows() {
		return pageRows;
	}


	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}


	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}
}
