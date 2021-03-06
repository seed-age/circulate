package com.sunnsoft.sloa.schedule;

import com.sunnsoft.ThirdPartyConfiguration;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.helper.HrmsubcompanyBean;
import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.util.SpringUtils;
import localhost.services.hrmservice.HrmService;
import localhost.services.hrmservice.HrmServicePortType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weaver.hrm.webservice.DepartmentBean;
import weaver.hrm.webservice.SubCompanyBean;
import weaver.hrm.webservice.UserBean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 每天凌晨 1点 定时更新 联系人 信息
 *
 * @author chenjian
 *
 */
@Service
public class HrmUserSchedule {
	private static final Log LOGGER = LogFactory.getLog(HrmUserSchedule.class);
	@Resource
	private UserService userService;

	@Value("${schedule.on}")
	private boolean scheduleOn;

	@PostConstruct
	private void init() {
		LOGGER.warn("初始化OA联系人!");
		try {
			this.doJob();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("================初始化OA联系人失败=====================");
		}
	}

	//	@Scheduled(cron = "0/10 * * * * ?")
	public void test() {
		//System.out.println("thirdPartyConfiguration: " + SpringUtils.getBean(ThirdPartyConfiguration.class));
		LOGGER.warn("thirdPartyConfiguration:" + SpringUtils.getBean(ThirdPartyConfiguration.Configuration.class));
	}

	@Scheduled(cron = "0 30 2 * * ?")
	public void doJob() {
		if (!scheduleOn) {// 开关没打开则不跑定时任务。
			return;
		}
		LOGGER.warn("定时任务被调用了!!");
		// ip地址
		//String ip = "192.168.4.183"; // 测试环境
		//String ip = "https://oa-uat.seedland.cc:8443/services/HrmService"; // 测试环境
		//String ip = "oa-uat.seedland.cc:8443"; // 生产环境
		//String ip = ThirdPartyConfiguration.getOaHrmSchedulerUrl();//"oa.seedland.cc"; // 生产环境
        String ip = "oa.seedland.cc";// 生产环境
//		String ip = "192.168.64.40";// 测试环境

		HrmService client = new HrmService();
		HrmServicePortType service = client.getHrmServiceHttpPort();

		//分部信息
		List<SubCompanyBean> subCompanyBean = service.getHrmSubcompanyInfo(ip).getSubCompanyBean();
		if(subCompanyBean != null && subCompanyBean.size() > 0){
			Services.hrmsubcompanyHelper().delete();
		}
		for (SubCompanyBean subCompany : subCompanyBean) {
			//新增分部信息
			String showorder = subCompany.getShoworder().getValue();
			Integer showorderInt = 10000;
			if(showorder != null && !"".equals(showorder)){
				showorderInt = Integer.valueOf(showorder);
			}

			String canceled = subCompany.getCanceled().getValue();
			if (canceled != null && !canceled.equals("")) {
				LOGGER.warn("分部 Canceled的值: " + canceled);
				if(canceled.equals("1")){
					LOGGER.warn("===========如果 分部 Canceled的值为 1 的话  就跳过本次循环  不进行新增===================");
					continue;
				}
			}
			Services.hrmsubcompanyHelper().bean().create()
					.setSubcompanyname(subCompany.getShortname().getValue())
					.setShoworder(showorderInt)
					.setId(Integer.valueOf(subCompany.getSubcompanyid().getValue()))
					.setSupsubcomid(Integer.valueOf(subCompany.getSupsubcompanyid().getValue()))
					.insertUnique();

		}

		//部门信息
		List<DepartmentBean> departmentBeans = service.getHrmDepartmentInfo(null, null).getDepartmentBean();
		if(departmentBeans != null && departmentBeans.size() > 0){
			Services.hrmdepartmentHelper().delete();
		}
		for (DepartmentBean departmentBean : departmentBeans) {
			//新增部门信息
			String showorder = departmentBean.getShoworder().getValue();
			Integer showorderInt = 10000;
			if(showorder != null && !"".equals(showorder)){
				showorderInt = Integer.valueOf(showorder);
			}

			String canceled = departmentBean.getCanceled().getValue();
			if (canceled != null && !canceled.equals("")) {
				LOGGER.warn("部门 Canceled的值: " + canceled);
				if(canceled.equals("1")){
					LOGGER.warn("===========如果 部门 Canceled的值为 1 的话  就跳过本次循环  不进行新增===================");
					continue;
				}
			}
			Services.hrmdepartmentHelper().bean().create()
					.setDepartmentname(departmentBean.getShortname().getValue())
					.setShoworder(showorderInt)
					.setId(Integer.valueOf(departmentBean.getDepartmentid().getValue()))
					.setSupdepid(Integer.valueOf(departmentBean.getSupdepartmentid().getValue()))
					.setSubcompanyid1(Integer.valueOf(departmentBean.getSubcompanyid().getValue()))
					.insertUnique();
		}

		//1. 查询出部门下的人员信息, 并且展示到前端
		int count = 0;
		// 获取到人员集合对象
		List<UserBean> userBeanList = service.getHrmUserInfo(ip, null, null, null, null, null).getUserBean();
		if(userBeanList != null && userBeanList.size() >0) {
			// 清空表数据
			Services.getUserMssageService().createHelper().delete();
		}
		// 遍历取出需要的数据
		for (UserBean userBean : userBeanList) {
			// 添加数据
			UserMssage userMssage = Services.getUserMssageService().createHelper().bean().create()
					.setUserId(userBean.getUserid()).setWorkCode(userBean.getWorkcode().getValue())
					.setLastName(userBean.getLastname().getValue()).setLoginId(userBean.getLoginid().getValue())
					.setDeptFullname(userBean.getDepartmentname().getValue())
					.setDepartmentId(userBean.getDepartmentid().getValue())
					.setSubcompanyId1(userBean.getSubcompanyid1().getValue())
					.setDsporder(userBean.getDsporder().floatValue())
					.setFullName(userBean.getSubcompanyname().getValue()).setStatus(userBean.getStatus().getValue())
					.setMobile(userBean.getMobile().getValue())
					.insertUnique();
			if (userMssage != null) {
				count++;
			}
		}
		System.out.println("添加人员总数： " + count);
		System.out.println("定时更新用户信息完成!");
	}

}
