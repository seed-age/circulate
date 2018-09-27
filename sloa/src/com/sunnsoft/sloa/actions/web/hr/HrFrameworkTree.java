package com.sunnsoft.sloa.actions.web.hr;

import com.alibaba.fastjson.JSON;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;
import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.helper.HrmdepartmentHelper;
import com.sunnsoft.sloa.helper.HrmsubcompanyHelper;
import com.sunnsoft.sloa.helper.UserMssageHelper;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.helper.json.EachEntity2Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HrFrameworkTree extends BaseParameter{

	private static final long serialVersionUID = 1L;

	private Integer supsubcomid;//分部ID
	private Integer departmentid;//部门ID
	@Override
	public String execute() throws Exception {
		HrmsubcompanyHelper hrmsubcompanyHelper = Services.hrmsubcompanyHelper();
		HrmdepartmentHelper hrmdepartmentHelper = Services.hrmdepartmentHelper();
		UserMssageHelper userMssageHelper = Services.userMssageHelper();
		List<Map<String, Object>> listData = new ArrayList<>();
		if(supsubcomid != null && departmentid == null){
			Map<String, Object> hrmdepartmentMap = null;
			List<Hrmdepartment> hrmdepartments = hrmdepartmentHelper
					.getSubcompanyid1().Eq(supsubcomid).getSupdepid().Eq(0).getShoworder().Asc().getId().Asc().list();
			for (Hrmdepartment hrmdepartment : hrmdepartments) {
				hrmdepartmentMap = new HashMap<>();
				hrmdepartmentMap.put("departmentid", hrmdepartment.getId());
				hrmdepartmentMap.put("departmentname", hrmdepartment.getDepartmentname());
				listData.add(hrmdepartmentMap);
			}
			
			hrmsubcompanyHelper.getSupsubcomid().Eq(supsubcomid);
			List<Hrmsubcompany> hrmsubcompanys = hrmsubcompanyHelper.getShoworder().Desc().getId().Asc().list();
			Map<String, Object> hrmsubcompanyMap = null;
			for (Hrmsubcompany hrmsubcompany : hrmsubcompanys) {
				hrmsubcompanyMap = new HashMap<>();
				hrmsubcompanyMap.put("subcompanyname", hrmsubcompany.getSubcompanyname());
				hrmsubcompanyMap.put("supsubcomid", hrmsubcompany.getId());
				listData.add(hrmsubcompanyMap);
			}
			
			json = JSON.toJSONString(listData);
		}else if(supsubcomid == null && departmentid == null){
			json = hrmsubcompanyHelper.getSupsubcomid().Eq(0).getShoworder().Asc().getId().Asc().json().listJson(new EachEntity2Map<Hrmsubcompany>() {
				@Override
				public void each(Hrmsubcompany hrmsubcompany, Map<String, Object> map) {
					if(map != null){
						map.clear();
						map.put("subcompanyname", hrmsubcompany.getSubcompanyname());
						map.put("supsubcomid", hrmsubcompany.getId());
					}
				}
			});
		}
		if(departmentid != null){
			List<UserMssage> userMssages = userMssageHelper
					.startOr().getStatus().Eq("0").getStatus().Eq("1")
					.getStatus().Eq("2").getStatus().Eq("3").stopOr()
					.getDepartmentId().Eq(departmentid.toString())
					.getDsporder().Asc().list();
			Map<String, Object> userMssagetMap = null;
			for (UserMssage userMssage : userMssages) {
				userMssagetMap = new HashMap<>();
				userMssagetMap.put("userId", userMssage.getUserId());
				userMssagetMap.put("lastName", userMssage.getLastName());
				userMssagetMap.put("departmentId", userMssage.getDepartmentId());
				listData.add(userMssagetMap);
			}
			
			Map<String, Object> hrmdepartmentMap = null;
			List<Hrmdepartment> hrmdepartments = hrmdepartmentHelper.getSupdepid().Eq(departmentid)
					.getShoworder().Desc().getId().Asc().list();
			for (Hrmdepartment hrmdepartment : hrmdepartments) {
				hrmdepartmentMap = new HashMap<>();
				hrmdepartmentMap.put("departmentid", hrmdepartment.getId());
				hrmdepartmentMap.put("departmentname", hrmdepartment.getDepartmentname());
				listData.add(hrmdepartmentMap);
			}
			
			json = JSON.toJSONString(listData);
		}
		msg = "查询成功";
		success = true;
		code = "200";
		return Results.GLOBAL_FORM_JSON;
	}
	public Integer getSupsubcomid() {
		return supsubcomid;
	}
	public void setSupsubcomid(Integer supsubcomid) {
		this.supsubcomid = supsubcomid;
	}
	public Integer getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(Integer departmentid) {
		this.departmentid = departmentid;
	}

}
