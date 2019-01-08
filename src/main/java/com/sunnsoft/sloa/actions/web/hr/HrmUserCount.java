package com.sunnsoft.sloa.actions.web.hr;

import com.alibaba.fastjson.JSON;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;
import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
import com.sunnsoft.sloa.util.HrmUtils;
import com.sunnsoft.util.struts2.Results;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HrmUserCount
 * @Description: 统计选择的 分部, 部门 的人员数量
 * @Auther: chenjian
 * @Date: 2019/1/8 9:50
 * @Version: 1.0
 **/
public class HrmUserCount extends BaseParameter {

    private Integer supsubcomid;//分部ID
    private Integer departmentid;//部门ID
    private boolean openType = false; // 默认不加载分部和部门的数量


    @Override
    public String execute() throws Exception {

        if (openType){ // 为true, 则加载整个组织架构的人员数量

            long count = Services.getUserMssageService().createHelper().startOr().getStatus().Eq("0").getStatus().Eq("1")
                    .getStatus().Eq("2").getStatus().Eq("3").stopOr().rowCount();

            Map<String, Object> map = new HashMap<>();
            map.put("count", count);
            json = JSON.toJSONString(map);

        }else if (supsubcomid != null) {

            Map<String, Object> map = new HashMap<>();
            Hrmsubcompany hrmsubcompany = Services.getHrmsubcompanyService().findById(supsubcomid);
            HrmUtils.subcompanyData(map, hrmsubcompany, true);

            json = JSON.toJSONString(map);


        }else if (departmentid != null) {
            Map<String, Object> map = new HashMap<>();
            Hrmdepartment hrmdepartment = Services.getHrmdepartmentService().findById(departmentid);
            Map<String, Object> departmentData = HrmUtils.departmentData(map, hrmdepartment, true);

            json = JSON.toJSONString(departmentData);

        }else {

            json = "null";
            code = "205";
            msg = "统计数量失败, 参数传递错误";
            return Results.GLOBAL_FAILURE_JSON;
        }

        success = true;
        code = "200";
        msg = "统计数量成功";
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

    public boolean isOpenType() {
        return openType;
    }

    public void setOpenType(boolean openType) {
        this.openType = openType;
    }
}
