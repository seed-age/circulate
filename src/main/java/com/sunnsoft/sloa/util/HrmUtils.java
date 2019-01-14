package com.sunnsoft.sloa.util;

import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Hrmdepartment;
import com.sunnsoft.sloa.db.vo.Hrmsubcompany;
import com.sunnsoft.sloa.db.vo.UserMssage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName HrmUtils
 * @Description: 关于获取组织架构数据的工具类
 * @Auther: chenjian
 * @Date: 2018/12/22 14:33
 * @Version: 1.0
 **/
public class HrmUtils {



    /**
     * @Author chenjian
     * @Description 递归获取当前分部下的所有人员信息
     * @Date 2018/12/24 11:32
     * @Param
     * @return
     **/
    public static void getSubcompanyUserMssage(Integer subcompanyId, List<Hrmdepartment> departments, Set<UserMssage> userMssageSet, Set<String> lastNameSet, int userId){

        if(subcompanyId != null && departments == null){
            // 获取当前分部下的所属部门
            List<Hrmdepartment> hrmdepartmentList = Services.getHrmdepartmentService().createHelper().getSubcompanyid1().Eq(subcompanyId).list();
            getHrmdepartmentData(hrmdepartmentList, userMssageSet, lastNameSet, userId);
        }else {

            getHrmdepartmentData(departments, userMssageSet, lastNameSet, userId);
        }

    }

    // 抽取重复代码
    private static void getHrmdepartmentData(List<Hrmdepartment> hrmdepartmentList, Set<UserMssage> userMssageSet, Set<String> lastNameSet, int userId) {

        for (Hrmdepartment hrmdepartment : hrmdepartmentList) {

            // 获取当前部门所属的人员
            List<UserMssage> userMssageList = Services.getUserMssageService().createHelper().getDepartmentId().Eq(hrmdepartment.getId() + "")
                    .startOr()
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
                    .stopOr()
                    .list();
            if (userMssageList.size() > 0) {
                for (UserMssage user : userMssageList) {

                    if (user.getUserId() == userId) { // 如果接受人中有发件人, 直接跳过
                        continue;
                    }

                    userMssageSet.add(user); // 重复的不可以添加
                    if (lastNameSet != null) {
//                        sb.append(user.getLastName()).append(";");
                        lastNameSet.add(user.getLastName());
                    }
                }
            }

            // 获取当前部门所属的下级部门
            List<Hrmdepartment> hrmdepartments = Services.getHrmdepartmentService().createHelper().getSupdepid().Eq(hrmdepartment.getId()).list();
            if(hrmdepartments.size() > 0 ){
                getSubcompanyUserMssage(null, hrmdepartments, userMssageSet, lastNameSet, userId);
            }
        }
    }

    /**
     * @Author chenjian
     * @Description 组装 分部的数据及统计分部下的人员数量
     * @Date 2018/12/22 14:53
     * @Param
     * @return
     **/
    public static void subcompanyData(Map<String, Object> hrmsubcompanyMap, Hrmsubcompany hrmsubcompany, boolean openType) {
        hrmsubcompanyMap.put("subcompanyname", hrmsubcompany.getSubcompanyname());
        hrmsubcompanyMap.put("supsubcomid", hrmsubcompany.getId());
        hrmsubcompanyMap.put("type", "subcompany");
        int countUser = 0;
        if (openType) {
            // 获取当前分部下的所属部门
            List<Hrmdepartment> hrmdepartmentList = Services.getHrmdepartmentService().createHelper().getSubcompanyid1().Eq(hrmsubcompany.getId()).list();
            for (Hrmdepartment hrmdepartment : hrmdepartmentList) {
                // 获取当前部门所属的人员
                countUser += (int) Services.getUserMssageService().createHelper().getDepartmentId().Eq(hrmdepartment.getId() + "")
                        .startOr()
                        .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
                        .getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS)
                        .getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
                        .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
                        .stopOr()
                        .rowCount();

                // 获取当前部门所属的下级部门
                List<Hrmdepartment> hrmdepartments = Services.getHrmdepartmentService().createHelper().getSupdepid().Eq(hrmdepartment.getId()).list();
                if(hrmdepartments.size() > 0 ){
                    getUserCount(hrmdepartments, countUser);
                }
            }
        }
        hrmsubcompanyMap.put("count", countUser);

    }

    /**
     * @Author chenjian
     * @Description 组装 部们的数据及统计部们下的人员数量
     * @Date  2018/12/22 14:53
     * @Param
     * @return
     **/
    public static Map<String, Object> departmentData(Map<String, Object> hrmdepartmentMap, Hrmdepartment hrmdepartment, boolean openType) {

        hrmdepartmentMap = new HashMap<>();
        hrmdepartmentMap.put("departmentid", hrmdepartment.getId());
        hrmdepartmentMap.put("departmentname", hrmdepartment.getDepartmentname());
        hrmdepartmentMap.put("type", "department");
        int countUser = 0;
        if (openType) {

            countUser += (int) Services.getUserMssageService().createHelper().getDepartmentId().Eq(hrmdepartment.getId() + "")
                    .startOr()
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
                    .stopOr()
                    .rowCount();

            // 获取当前部门所属的下级部门
            List<Hrmdepartment> hrmdepartmentList = Services.getHrmdepartmentService().createHelper().getSupdepid().Eq(hrmdepartment.getId()).list();
            if (hrmdepartmentList.size() > 0) {
                hrmdepartmentMap.put("count", getUserCount(hrmdepartmentList, countUser));
            }else {
                hrmdepartmentMap.put("count", countUser);
            }
        }else {
            hrmdepartmentMap.put("count", 0);
        }

        return hrmdepartmentMap;
    }

    /**
     * @Author chenjian
     * @Description 查询部们下的所有人员信息和下属部门. (递归)
     * @Date  2018/12/22 14:54
     * @Param
     * @return
     **/
    public static int getUserCount(List<Hrmdepartment> hrmdepartments, int countUser){

        for (Hrmdepartment hrmdepartment : hrmdepartments) {
            // 2. 根据当前部门ID查询所属的人员及其下属部门
            // 2.1. 人员
            countUser += (int) Services.getUserMssageService().createHelper().getDepartmentId().Eq(hrmdepartment.getId() + "")
                    .startOr()
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
                    .getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
                    .stopOr()
                    .rowCount();
            // 2.2. 下属部门
            // 因为实地组织架构表的关系, 第二级的部门的上级为 subcompanyid1字段, 而部门所属的下级部门为 supdepid 字段
            List<Hrmdepartment> hrmdepList = Services.getHrmdepartmentService().createHelper().getSupdepid().Eq(hrmdepartment.getId()).list();
            if(hrmdepList.size() > 0){
                countUser += getUserCount(hrmdepList, countUser);
            }
        }

        return countUser;
    }

}
