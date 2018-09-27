package com.sunnsoft.test;

import java.util.List;

import localhost.services.hrmservice.HrmService;
import localhost.services.hrmservice.HrmServicePortType;
import weaver.hrm.webservice.ArrayOfDepartmentBean;
import weaver.hrm.webservice.ArrayOfJobTitleBean;
import weaver.hrm.webservice.ArrayOfSubCompanyBean;
import weaver.hrm.webservice.ArrayOfUserBean;
import weaver.hrm.webservice.DepartmentBean;
import weaver.hrm.webservice.JobTitleBean;
import weaver.hrm.webservice.SubCompanyBean;
import weaver.hrm.webservice.UserBean;
public class Test {

	public static void main(String[] args) {
		Integer mailStatus = 6;
		switch (mailStatus) {
		case 0:
			System.out.println("我是 零 ");
			break;
		case 1:
			System.out.println("我是 壹 ");
			break;
		case 2:
			
			System.out.println("我是 贰 ");
			break;
		case 3:
			System.out.println("我是 叁 ");
			break;

		default:
			System.out.println("我是 默认 ");
			break;
		}
	}

	public static void ssssss() {
		String orgxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<root>" + 
				"<orglist>" + 
				"<org action=\"add\">" + "<code>"+"001"+"</code>" + 
				"        <shortname>测试分部一</shortname>" + 
				"        <fullname>测试分部一</fullname>" + 
				"        <parent_code>0</parent_code>" + 
				"        <order>0</order>" + 
				"     </org>" + 
				"     <org action=\"edit\">" + 
				"        <code>002</code>" + 
				"        <shortname>测试分部二</shortname>" + 
				"        <fullname>测试分部二</fullname>" + 
				"        <parent_code>0</parent_code>" + 
				"        <order>1</order>" + 
				"     </org>" + 
				"     <org action=\"delete\">" + 
				"        <code>003</code>" + 
				"        <canceled>1</canceled>" + 
				"     </org>" + 
				"  </orglist>" + 
				"</root>";
		HrmService client = new HrmService();
		HrmServicePortType service = client.getHrmServiceHttpPort();
		
		//1. 参数: String ipaddress：调用接口的IP地址，String xmlData：分部信息的xml  
		//返回值: 成功:1,失败:0,2:无权限调用
		//用途: 同步分部信息   
		String subCompany = service.synSubCompany("119.23.219.65", orgxml);
		System.out.println("同步信息: "+subCompany);
	}

	public static void sss(String orgxml, HrmServicePortType service) {
		//2. 参数: String ipaddress：调用接口的IP地址，String xmlData：分部信息的xml  
		//返回值: 成功:1,失败:0,2:无权限调用
		//用途: 同步部门信息 
		String synDepartment = service.synDepartment("",orgxml);
		
		//参数: String ipaddress：调用接口的IP地址，String xmlData：分部信息的xml  
		//返回值: 成功:1,失败:0,2:无权限调用
		//用途: 同步岗位信息
		String synJobtitle = service.synJobtitle("", orgxml);
		
		
		//参数: String ipaddress：调用接口的IP地址
		//返回值: SubCompanyInfo[]:  分部信息列表
		//用途: 获取所有分部信息列表
		ArrayOfSubCompanyBean companyBean = service.getHrmSubcompanyInfo("");
		List<SubCompanyBean> subCompanyBean = companyBean.getSubCompanyBean();
		
		
		//参数: String ipaddress：调用接口的IP地址，String subcompanyId：分部id，多个用逗号分隔（不为空时该条件有效）  
		//返回值: DepartmentInfo[]：部门信息列表
		//用途: 根据参数条件获取部门信息列表
		ArrayOfDepartmentBean ofDepartmentBean = service.getHrmDepartmentInfo("", "");
		List<DepartmentBean> departmentBean = ofDepartmentBean.getDepartmentBean();
		
		
		//参数: String ipaddress：调用接口的IP地址，String subcompanyId：分部id，多个用逗号分隔（不为空时该条件有效）  
		//返回值: JobTitleInfo[]:岗位信息列表
		//用途: 根据参数条件获取岗位信息列表
		ArrayOfDepartmentBean hrmDepartmentInfo = service.getHrmDepartmentInfo("", "");
		List<DepartmentBean> list = hrmDepartmentInfo.getDepartmentBean();

		
		/**参数: 
		 * String ipaddress：调用接口的IP地址，
		 * String subcompanyId：分部id，多个用逗号分隔（不为空时该条件有效）
		 * String departmentid：部门id，多个用逗号分隔（不为空时该条件有效） */
		//返回值: JobTitleInfo[]:岗位信息列表
		//用途: 根据参数条件获取岗位信息列表
		ArrayOfJobTitleBean hrmJobTitleInfo = service.getHrmJobTitleInfo("", "", "");
		List<JobTitleBean> jobTitleBean = hrmJobTitleInfo.getJobTitleBean();
		
		/**
		 * 参数:   
		 * String ipaddress：	     调用接口的IP地址，
		 * String subcompanyId：	     分部id，多个用逗号分隔（不为空时该条件有效）
		 * String departmentid：      部门id，多个用逗号分隔（不为空时该条件有效）
		 * String jobtitleid：           岗位id，多个用逗号分隔（不为空时该条件有效）
		 * String lastChangeDate： 最后修改日期，日期格式“YYYY-MM-DD”（不为空时该条件有效）
		 * String workcode:       人员编码（不为空时该条件有效）
		 */
		//返回值: UserInfo[]：用户信息列表
		//用途: 根据参数条件获取用户信息列表
		ArrayOfUserBean hrmUserInfo = service.getHrmUserInfo("", "", "", "", "", "");
		List<UserBean> userBean = hrmUserInfo.getUserBean();
		
		/**
		 * 参数:
		 * String ipaddress：调用接口的IP地址
		 * String loginid：登录名 
		 * String password：密码  
		 */
		//返回值: True:表示存在    False：表示不存在
		//用途: 检测OA用户
		boolean checkUser = service.checkUser("", "", "");
	}
}
